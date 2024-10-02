import axios from 'axios';

const token = '\tBearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3QzQGV4YW1wbGUuY29tIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTcyNzgzNjcyNCwiZXhwIjoxNzI3ODcyNzI0fQ.xte3HqK47aD73hlWZ1QzYUVJdnIXJX2-7pYWGZ57FHY';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        Authorization: token,
    },
});

export default api;
