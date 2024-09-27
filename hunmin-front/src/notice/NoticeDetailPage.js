import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, Link, useNavigate } from 'react-router-dom';
import './NoticeDetailPage.css';

const NoticeDetailPage = () => {
    const { id } = useParams();
    const [notice, setNotice] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        axios.get(`/api/notices/${id}`)
            .then(response => {
                setNotice(response.data);
            })
            .catch(error => {
                console.error("There was an error fetching the notice!", error);
            });
    }, [id]);

    const handleDelete = () => {
        axios.delete(`/api/notices/${id}`, {
            data: { memberId: 1 } // 실제 멤버 ID를 여기에 넣어야 합니다.
        })
            .then(response => {
                if (response.data.result === 'success') {
                    alert('공지사항이 삭제되었습니다.');
                    navigate('/notices');
                } else {
                    alert('공지사항 삭제에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error("There was an error deleting the notice!", error);
                alert('공지사항 삭제에 실패했습니다.');
            });
    };
    const handleGoHome = () => {
        navigate('/notices');
    };



    if (!notice) return <div className="loading">Loading...</div>;

    return (
        <div className="container">
            <div className="banner">
                <div className="icon"></div>
                <div className="header-text">훈민정음 2.0</div>
            </div>
            <h1 className="title">{notice.title}</h1>
            <p className="content">{notice.content}</p>
            <Link to={`/edit-notice/${notice.noticeId}`} className="edit-link">공지사항 수정</Link>
            <button onClick={handleGoHome} className="home-button">공지사항 목록으로 돌아가기</button>
            <button onClick={handleDelete} className="delete-button">공지사항 삭제</button>

        </div>
    );
};

export default NoticeDetailPage;

