package com.example.springboard.service;

import com.example.springboard.domain.entity.BoardEntity;
import com.example.springboard.domain.repository.BoardRepository;
import com.example.springboard.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BoardService {
    private BoardRepository boardRepository;

    //페이징을 위한 추가
    private static final int BLOCK_PAGE_NUM_COUNT = 5; // 페이지 번호 수
    private static final int PAGE_POST_COUNT = 3; // 한 페이지에 존재하는 게시글의 수

    //DB에 게시글을 저장하는 서비스
    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    //게시판의 모든 자료를 조회하는 서비스
    @Transactional
    public List<BoardDto> getBoardList(int pageNum) {
        //0. 페이징을 위한 로직
        Page<BoardEntity> page = boardRepository.findAll(PageRequest.of(pageNum-1, PAGE_POST_COUNT, Sort.by(Sort.Direction.DESC, "createdDate")));

        //1. boardEntity 자료형의  리스트
        List<BoardEntity> boardEntities = page.getContent();
//      List<BoardEntity> boardEntities = boardRepository.findAll();

        //2. boardDto 자료형의 List
        List<BoardDto> boardDtoList = new ArrayList<>();

        //3. Conrtoller와 Service 간의 데이터 전달은 Dto로 이루어져야함.
        //   Repository로 가져온 Entity를 반복문을 통해 Dto로 변환한 후, 전달
        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    //게시글의 상세 정보를 조회하는 서비스
    @Transactional
    public BoardDto getPost(Long id) {
        Optional<BoardEntity> boardEntityWrapper = boardRepository.findById(id);

        BoardEntity boardEntity = boardEntityWrapper.get();

        BoardDto boardDto = BoardDto.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .createdDate(boardEntity.getCreatedDate())
                .modifiedDate(boardEntity.getModifiedDate())
                .build();

        return boardDto;
    }

    //게시글을 삭제하는 서비스
    @Transactional
    public void deletePost(Long id) {
        boardRepository.deleteById(id);
    }

    //게시글을 검색하는 서비스
    @Transactional
    public List<BoardDto> searchPosts(String keyword) {
        List<BoardEntity> boardEntities = boardRepository.findByTitleContaining(keyword);

        List<BoardDto> boardDtoList = new ArrayList<>();

        if(boardEntities.isEmpty()) return boardDtoList; //아무것도 존재하지 않을 경우

        for (BoardEntity boardEntity : boardEntities) {
            boardDtoList.add(this.convertEntityToDto(boardEntity));
        }

        return boardDtoList;
    }

    //총 게시글을 조회하는 함수
    @Transactional
    public Long getBoardCount() {
        return  boardRepository.count();
    }

    //페이지 수를 받아오는 함수
    public Integer[] getPageList(Integer curPageNum) {

        Integer[] pageList = new Integer[BLOCK_PAGE_NUM_COUNT];

        //1. 총 게시글 갯수
        Double postTotalCount = Double.valueOf(this.getBoardCount());

        //2. 총 게시글 기준으로 마지막 페이지 번호 계산
        Integer totalLastPageNum = (int)(Math.ceil((postTotalCount/PAGE_POST_COUNT)));

        //3. 현재 페이지 기준 블럭의 마지막 페이지 번호 계산
        Integer blockLastPageNum = (totalLastPageNum > curPageNum + BLOCK_PAGE_NUM_COUNT) ? curPageNum + BLOCK_PAGE_NUM_COUNT : totalLastPageNum;

        //4. 페이지 시작 번호 조정
        curPageNum = (curPageNum <= 3) ? 1 : curPageNum - 2;

        //5. 페이지 번호 할당
        for(int val = curPageNum, idx = 0; val <= blockLastPageNum; val++, idx++) {
            pageList[idx] = val;
        }

        return pageList;

    }

    //ConvertEntityToDto : Entity를 Dto로 변환하는 함수
    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        return BoardDto.builder()
                .id(boardEntity.getId())
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .writer(boardEntity.getWriter())
                .createdDate(boardEntity.getCreatedDate())
                .build();
    }
}
