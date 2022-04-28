package com.bamdoliro.stupetition.domain.board.service;

import com.bamdoliro.stupetition.domain.board.domain.Board;
import com.bamdoliro.stupetition.domain.board.domain.BoardJoiner;
import com.bamdoliro.stupetition.domain.board.domain.repository.BoardJoinerRepository;
import com.bamdoliro.stupetition.domain.board.exception.SameBoardWriterAndAgreerException;
import com.bamdoliro.stupetition.domain.board.exception.UserAlreadyJoinException;
import com.bamdoliro.stupetition.domain.board.presentation.dto.request.JoinBoardRequestDto;
import com.bamdoliro.stupetition.domain.user.domain.User;
import com.bamdoliro.stupetition.domain.user.domain.type.Authority;
import com.bamdoliro.stupetition.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardJoinerService {

    private final BoardJoinerRepository boardJoinerRepository;
    private final UserService userService;
    private final BoardService boardService;

    @Transactional
    public void joinBoard(JoinBoardRequestDto dto) {
        User user = userService.getCurrentUser();
        Board board = boardService.getBoard(dto.getBoardId());
        validateJoinUserToBoard(user, board);

        boardJoinerRepository.save(createBoardByAuthority(user, board, dto));
    }

    private void validateJoinUserToBoard(User user, Board board) {
        if (boardJoinerRepository.existsBoardJoinerByUserAndBoard(user, board)) {
            throw UserAlreadyJoinException.EXCEPTION;
        }
        if (board.getUser().equals(user)) {
            throw SameBoardWriterAndAgreerException.EXCEPTION;
        }
    }


    private BoardJoiner createBoardByAuthority(User user, Board board, JoinBoardRequestDto dto) {
        if (user.getAuthority() == Authority.ROLE_STUDENT) {
            board.addAgreer();
        }

        return BoardJoiner.builder()
                .user(user)
                .board(board)
                .comment(dto.getComment())
                .isStudentCouncil(user.getAuthority() == Authority.ROLE_STUDENT_COUNCIL)
                .build();
    }

}
