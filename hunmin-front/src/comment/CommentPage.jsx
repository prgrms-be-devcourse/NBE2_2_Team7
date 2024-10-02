import React, { useState, useEffect } from 'react';
import {
    createComment,
    createChildComment,
    updateComment,
    deleteComment,
    getCommentsByBoard
} from './CommentService';
import {
    Typography,
    TextField,
    Button,
    List,
    ListItem,
    ListItemText,
    ListItemSecondaryAction,
    IconButton,
    Paper,
    Divider,
    Pagination,
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ReplyIcon from '@mui/icons-material/Reply';

const CommentPage = ({ boardId }) => {
    const [comments, setComments] = useState([]);
    const [content, setContent] = useState('');
    const [editCommentId, setEditCommentId] = useState(null);
    const [replyCommentId, setReplyCommentId] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        fetchComments(currentPage);
    }, [boardId, currentPage]);

    const fetchComments = async (page) => {
        try {
            const response = await getCommentsByBoard(boardId, { page, size: 5 });
            setComments(response.data.content);
            setTotalPages(response.data.totalPages); // 전체 페이지 수 설정
        } catch (error) {
            console.error('Error fetching comments:', error);
        }
    };

    const handleCreate = async () => {
        try {
            const memberId = localStorage.getItem('memberId'); // localStorage에서 memberId 가져오기
            const commentData = { content, memberId }; // memberId를 commentData에 포함
            await createComment(boardId, commentData);
            setContent('');
            fetchComments(currentPage); // 현재 페이지로 댓글 새로고침
        } catch (error) {
            console.error('Error creating comment:', error);
        }
    };

    const handleReply = async (commentId) => {
        try {
            const memberId = localStorage.getItem('memberId'); // localStorage에서 memberId 가져오기
            const commentData = { content, memberId }; // memberId를 commentData에 포함
            await createChildComment(boardId, commentId, commentData);
            setContent('');
            setReplyCommentId(null);
            fetchComments(currentPage);
        } catch (error) {
            console.error('Error replying to comment:', error);
        }
    };

    const handleEdit = async () => {
        try {
            const memberId = localStorage.getItem('memberId'); // localStorage에서 memberId 가져오기
            const commentData = { content, memberId }; // memberId를 commentData에 포함
            await updateComment(boardId, editCommentId, commentData);
            setContent('');
            setEditCommentId(null);
            fetchComments(currentPage);
        } catch (error) {
            console.error('Error updating comment:', error);
        }
    };

    const handleDelete = async (commentId) => {
        try {
            await deleteComment(boardId, commentId);
            fetchComments(currentPage);
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    const renderComments = (comments, level = 0) => {
        return comments.map((comment) => {
            const displayDate = comment.updatedAt
                ? new Date(comment.updatedAt).toLocaleString()
                : new Date(comment.createdAt).toLocaleString();

            return (
                <div key={comment.commentId}>
                    <ListItem divider style={{ marginLeft: level * 20 }}> {/* 계단식 들여쓰기 */}
                        <ListItemText
                            primary={`${comment.nickname}: ${comment.content}`}
                            secondary={`작성일: ${displayDate}`}
                        />
                        <ListItemSecondaryAction>
                            <IconButton edge="end" onClick={() => setEditCommentId(comment.commentId)}>
                                <EditIcon />
                            </IconButton>
                            <IconButton edge="end" onClick={() => handleDelete(comment.commentId)}>
                                <DeleteIcon />
                            </IconButton>
                            <IconButton edge="end" onClick={() => setReplyCommentId(comment.commentId)}>
                                <ReplyIcon />
                            </IconButton>
                        </ListItemSecondaryAction>
                    </ListItem>

                    {replyCommentId === comment.commentId && (
                        <div style={{ marginLeft: '40px', marginTop: '10px' }}>
                            <TextField
                                fullWidth
                                placeholder="답글을 입력하세요..."
                                variant="outlined"
                                value={content}
                                onChange={(e) => setContent(e.target.value)}
                            />
                            <Button onClick={() => handleReply(comment.commentId)} variant="contained" color="primary">
                                답글
                            </Button>
                        </div>
                    )}

                    {comment.children && comment.children.length > 0 && (
                        <List>
                            {renderComments(comment.children, level + 1)} {/* 대댓글에 대한 레벨 증가 */}
                        </List>
                    )}
                </div>
            );
        });
    };

    const handlePageChange = (event, newPage) => {
        if (newPage > 0 && newPage <= totalPages) {
            setCurrentPage(newPage);
        }
    };

    return (
        <Paper style={{ padding: '20px' }}>
            <hr/>
            <List>
                {renderComments(comments)}
            </List>
            <Divider />
            <div style={{ marginTop: '20px' }}>
                <TextField
                    fullWidth
                    placeholder="댓글을 입력하세요..."
                    variant="outlined"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                />
                {editCommentId ? (
                    <Button onClick={handleEdit} variant="contained" color="primary">댓글 수정</Button>
                ) : (
                    <Button onClick={handleCreate} variant="contained" color="primary">댓글 추가</Button>
                )}
            </div>
            <Pagination
                count={totalPages}
                page={currentPage}
                onChange={handlePageChange}
                variant="outlined"
                color="primary"
                style={{ marginTop: '20px' }}
            />
        </Paper>
    );
};

export default CommentPage;
