import React, { useState, useEffect } from 'react';
import {
    createComment,
    createChildComment,
    updateComment,
    deleteComment,
    getCommentsByBoard
} from './CommentService';

const CommentPage = ({ boardId }) => {
    const [comments, setComments] = useState([]);
    const [content, setContent] = useState('');
    const [editCommentId, setEditCommentId] = useState(null);
    const [replyCommentId, setReplyCommentId] = useState(null);

    useEffect(() => {
        fetchComments();
    }, [boardId]);

    const fetchComments = async () => {
        try {
            const response = await getCommentsByBoard(boardId, { page: 1, size: 5 });
            setComments(response.data.content);
        } catch (error) {
            console.error('Error fetching comments:', error);
        }
    };

    const handleCreate = async () => {
        try {
            const commentData = { content, memberId: 1 }; // Replace memberId with actual userId
            await createComment(boardId, commentData);
            setContent('');
            fetchComments();
        } catch (error) {
            console.error('Error creating comment:', error);
        }
    };

    const handleReply = async (commentId) => {
        try {
            const commentData = { content, memberId: 1 }; // Replace with actual userId
            await createChildComment(boardId, commentId, commentData);
            setContent('');
            setReplyCommentId(null);
            fetchComments();
        } catch (error) {
            console.error('Error replying to comment:', error);
        }
    };

    const handleEdit = async () => {
        try {
            const commentData = { content };
            await updateComment(boardId, editCommentId, commentData); // boardId와 editCommentId 전달
            setContent('');
            setEditCommentId(null);
            fetchComments();
        } catch (error) {
            console.error('Error updating comment:', error);
        }
    };

    const handleDelete = async (commentId) => {
        try {
            await deleteComment(boardId, commentId); // boardId와 commentId 전달
            fetchComments();
        } catch (error) {
            console.error('Error deleting comment:', error);
        }
    };

    const renderComments = (comments) => {
        return comments.map((comment) => {
            const displayDate = comment.updatedAt
                ? new Date(comment.updatedAt).toLocaleString()
                : new Date(comment.createdAt).toLocaleString();

            return (
                <li key={comment.commentId}>
                    <div>
                        <span>{comment.nickname}:</span>
                        <span>{comment.content}</span>
                        <div>
                            <small>{`작성일: ${displayDate}`}</small> {/* 조건에 따라 작성일 또는 수정일 표시 */}
                        </div>
                        <button onClick={() => setEditCommentId(comment.commentId)}>수정</button>
                        <button onClick={() => handleDelete(comment.commentId)}>삭제</button>
                        <button onClick={() => setReplyCommentId(comment.commentId)}>댓글</button>
                    </div>

                    {replyCommentId === comment.commentId && (
                        <div style={{ marginLeft: '20px' }}>
                        <textarea
                            value={content}
                            onChange={(e) => setContent(e.target.value)}
                        />
                            <button onClick={() => handleReply(comment.commentId)}>Reply</button>
                        </div>
                    )}

                    {/* 대댓글이 있는 경우 재귀적으로 렌더링 */}
                    {comment.children && comment.children.length > 0 && (
                        <ul style={{ marginLeft: '20px' }}>
                            {renderComments(comment.children)}
                        </ul>
                    )}
                </li>
            );
        });
    };


    return (
        <div>
            <hr />
            <h3>댓글 목록</h3>
            <ul>
                {renderComments(comments)}
            </ul>

            <div>
                <textarea
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                />
                {editCommentId ? (
                    <button onClick={handleEdit}>Update Comment</button>
                ) : (
                    <button onClick={handleCreate}>Add Comment</button>
                )}
            </div>
        </div>
    );
};

export default CommentPage;
