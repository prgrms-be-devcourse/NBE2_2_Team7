import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';

const NoticeForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [notice, setNotice] = useState({ title: '', content: '' });

    useEffect(() => {
        if (id) {
            axios.get(`/api/notices/${id}`)
                .then(response => {
                    setNotice(response.data);
                })
                .catch(error => {
                    console.error("There was an error fetching the notice!", error);
                });
        }
    }, [id]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setNotice(prevNotice => ({
            ...prevNotice,
            [name]: value
        }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (id) {
            axios.put(`/api/notices/${id}`, notice)
                .then(response => {
                    navigate(`/notices/${id}`);
                })
                .catch(error => {
                    console.error("There was an error updating the notice!", error);
                });
        } else {
            axios.post('/api/notices', notice)
                .then(response => {
                    navigate(`/notices/${response.data.noticeId}`);
                })
                .catch(error => {
                    console.error("There was an error creating the notice!", error);
                });
        }
    };

    return (
        <form onSubmit={handleSubmit}>
            <div>
                <label>제목:</label>
                <input type="text" name="title" value={notice.title} onChange={handleChange} required />
            </div>
            <div>
                <label>내용:</label>
                <textarea name="content" value={notice.content} onChange={handleChange} required />
            </div>
            <button type="submit">{id ? '수정' : '생성'}</button>
        </form>
    );
};

export default NoticeForm;