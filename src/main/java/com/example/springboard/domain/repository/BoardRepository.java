package com.example.springboard.domain.repository;

import com.example.springboard.domain.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    //검색을 직접적으로 호출하는 메서드. JpaRepository에서 메서드 명의 By 이후는 SQL의 Where 조건 절에 대응
    //Containing을 붙여주면 Like 검색이 됨 -> %{keyword}%이 된다는 것.
    //그 외, Startswith(검색어로 시작하는 Like), Endswith, IgnoreCase(대소문자 구분 없이 검색), Not(검색어를 포함하지 않는 검색), Not
    List<BoardEntity> findByTitleContaining(String keyword);
}
