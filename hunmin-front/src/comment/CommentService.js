import api from '../axios';

const createComment = (boardId, commentData) => {
    return api.post(`/board/${boardId}/comment`, commentData); // Create comment on a board
};

const createChildComment = (boardId, commentId, commentData) => {
    return api.post(`/board/${boardId}/comment/${commentId}`, commentData); // Create child comment (reply)
};

const updateComment = (boardId, commentId, commentData) => {
    return api.put(`/board/${boardId}/comment/${commentId}`, commentData); // Update comment
};

const deleteComment = (boardId, commentId) => {
    return api.delete(`/board/${boardId}/comment/${commentId}`); // Delete comment
};

const getCommentsByBoard = (boardId, params) => {
    return api.get(`/board/${boardId}/comment`, { params }); // Get comments by board
};

export {
    createComment,
    createChildComment,
    updateComment,
    deleteComment,
    getCommentsByBoard
};
