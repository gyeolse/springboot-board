package com.example.springboard.service;

import com.example.springboard.domain.entity.BoardEntity;
import com.example.springboard.domain.repository.BoardRepository;
import com.example.springboard.dto.BoardDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BoardService {
    private BoardRepository boardRepository;

    //DB에 게시글을 저장하는 서비스
    @Transactional
    public Long savePost(BoardDto boardDto) {
        return boardRepository.save(boardDto.toEntity()).getId();
    }

    //게시판의 모든 자료를 조회하는 서비스
    @Transactional
    public List<BoardDto> getBoardList() {
        //1. boardEntity 자료형의  리스트
        List<BoardEntity> boardEntities = boardRepository.findAll();

        //2. boardDto 자료형의 List
        List<BoardDto> boardDtoList = new ArrayList<>();

        //3. Conrtoller와 Service 간의 데이터 전달은 Dto로 이루어져야함.
        //   Repository로 가져온 Entity를 반복문을 통해 Dto로 변환한 후, 전달
        for ( BoardEntity boardEntity : boardEntities) {
            BoardDto boardDto = BoardDto.builder()
                    .id(boardEntity.getId())
                    .title(boardEntity.getTitle())
                    .content(boardEntity.getContent())
                    .writer(boardEntity.getWriter())
                    .createdDate(boardEntity.getCreatedDate())
                    .build();

            boardDtoList.add(boardDto);
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
}
