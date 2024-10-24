import React, { useState, useEffect } from 'react';
import {
    createComment,
    createChildComment,
    updateComment,
    deleteComment,
    getCommentsByBoard,
    unlikeComment,
    likeComment,
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
import FavoriteBorderIcon from '@mui/icons-material/FavoriteBorder';
import FavoriteIcon from '@mui/icons-material/Favorite';
import api from '../axios';

const CommentPage = ({ boardId }) => {
    const [comments, setComments] = useState([]);
    const [content, setContent] = useState('');
    const [editCommentId, setEditCommentId] = useState(null);
    const [replyCommentId, setReplyCommentId] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const [editCommentMemberId, setEditCommentMemberId] = useState(null);

    useEffect(() => {
        fetchComments(currentPage);
    }, [boardId, currentPage]);

    const fetchComments = async (page) => {
        try {
            const memberId = localStorage.getItem('memberId');
            const response = await getCommentsByBoard(boardId, { page, size: 5 });
            const commentList = response.data.content;

            const updatedComments = await Promise.all(
                commentList.map(async (comment) => {
                    const isLikedResponse = await api.get(`/likeComment/${comment.commentId}/member/${memberId}`);
                    return {
                        ...comment,
                        isLiked: isLikedResponse.data,
                    };
                })
            );

            setComments(updatedComments);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error('Error fetching comments:', error);
        }
    };

    const updateCommentLikeStatus = (commentId, isLiked) => {
        const updateComments = (comments) => comments.map(comment => {
            if (comment.commentId === commentId) {
                return {
                    ...comment,
                    isLiked: !isLiked,
                    likeCount: isLiked ? comment.likeCount - 1 : comment.likeCount + 1
                };
            }
            if (comment.children) {
                return {
                    ...comment,
                    children: updateComments(comment.children),
                };
            }
            return comment;
        });

        setComments(prevComments => updateComments(prevComments));
    };

    const handleLikeToggle = async (commentId, isLiked) => {
        const memberId = localStorage.getItem('memberId');
        try {
            if (isLiked) {
                await unlikeComment(commentId);
            } else {
                await likeComment(commentId);
            }
            updateCommentLikeStatus(commentId, isLiked);
        } catch (error) {
            console.error('Error toggling like:', error);
        }
    };

    const handleCreate = async () => {
        try {
            const memberId = localStorage.getItem('memberId');
            const commentData = { content, memberId };
            await createComment(boardId, commentData);
            setContent('');
            fetchComments(currentPage);
        } catch (error) {
            console.error('Error creating comment:', error);
        }
    };

    const handleReply = async (commentId) => {
        try {
            const memberId = localStorage.getItem('memberId');
            const commentData = { content, memberId };
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
            const commentData = { content, memberId: editCommentMemberId };
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
                    <ListItem divider style={{ marginLeft: level * 20 }}>
                        <ListItemText
                            primary={`${comment.nickname}: ${comment.content}`}
                            secondary={`작성일: ${displayDate}`}
                        />
                        <ListItemSecondaryAction>
                            <IconButton onClick={() => handleLikeToggle(comment.commentId, comment.isLiked)}>
                                {comment.isLiked ? (
                                    <FavoriteIcon color="error"/> // 좋아요한 댓글은 빨간색 하트
                                ) : (
                                    <FavoriteBorderIcon/> // 좋아요하지 않은 댓글은 빈 하트
                                )}
                            </IconButton>
                            <span>{comment.likeCount || 0}</span>
                            <IconButton edge="end" onClick={() => handleEditClick(comment)}>
                                <EditIcon/>
                            </IconButton>
                            <IconButton edge="end" onClick={() => handleDelete(comment.commentId)}>
                                <DeleteIcon/>
                            </IconButton>
                            <IconButton edge="end" onClick={() => setReplyCommentId(comment.commentId)}>
                                <ReplyIcon/>
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
                            {renderComments(comment.children, level + 1)}
                        </List>
                    )}
                </div>
            );
        });
    };

    const handleEditClick = (comment) => {
        setEditCommentId(comment.commentId);
        setContent(comment.content);
        setEditCommentMemberId(comment.memberId);
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
