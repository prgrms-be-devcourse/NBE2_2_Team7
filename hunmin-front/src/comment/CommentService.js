import axios from 'axios';

const API_URL = 'http://localhost:8080/api/board'; // Base URL

const createComment = (boardId, commentData) => {
    return axios.post(`${API_URL}/${boardId}/comment`, commentData); // Create comment on a board
};

const createChildComment = (boardId, commentId, commentData) => {
    return axios.post(`${API_URL}/${boardId}/comment/${commentId}`, commentData); // Create child comment (reply)
};

const updateComment = (boardId, commentId, commentData) => {
    return axios.put(`${API_URL}/${boardId}/comment/${commentId}`, commentData); // Update comment
};

const deleteComment = (boardId, commentId) => {
    return axios.delete(`${API_URL}/${boardId}/comment/${commentId}`); // Delete comment
};

const getCommentsByBoard = (boardId, params) => {
    return axios.get(`${API_URL}/${boardId}/comment`, { params }); // Get comments by board
};

export {
    createComment,
    createChildComment,
    updateComment,
    deleteComment,
    getCommentsByBoard
};
