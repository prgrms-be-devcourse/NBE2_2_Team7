import React, { useEffect, useState } from 'react';
//import axios from 'axios';
import api from '../axios';
import { Link , useNavigate } from 'react-router-dom';
import './NoticeListPage.css';

const NoticeListPage = () => {
    const [notices, setNotices] = useState([]);
    const [page, setPage] = useState(1); // 초기 페이지 번호

    useEffect(() => {
        fetchNotices(page);
    }, [page]);

    const fetchNotices = (page) => {
        //axios.get(`/api/notices/list/${page}`)
        api.get(`/notices/list/${page}`)
            .then(response => {
                setNotices(response.data);
            })
            .catch(error => {
                if (error.response && error.response.data) {
                    alert(`Error: ${error.response.data.error}`);
                } else {
                    alert("There was an error fetching the notices!");
                }
            });
    };

    const handleNextPage = () => {
        setPage(prevPage => prevPage + 1);
    };

    const handlePreviousPage = () => {
        if (page > 1) {
            setPage(prevPage => prevPage - 1);
        }
    };


    return (
        <div className="container">
            <div className="banner">
                <div className="icon"></div>
                <div>훈민정음 2.0</div>
            </div>
            <h1 className="title">공지사항 목록</h1>
            <ul className="list">
                {notices.map(notice => (
                    <li key={notice.noticeId} className="list-item">
                        <Link to={`/notices/${notice.noticeId}`} className="styled-link">{notice.title}</Link>
                        <div className="notice-details">
                            <span>작성자: {notice.nickname}</span><br/>
                            <span>{new Date(notice.createdAt).toLocaleDateString()}</span>
                        </div>
                    </li>
                ))}
            </ul>
            <div className="pagination">
                <button onClick={handlePreviousPage} disabled={page === 1}>이전 페이지</button>
                <span>{page}</span>
                <button onClick={handleNextPage}>다음 페이지</button>
            </div>
            <Link to="/create-notice" className="create-link">공지사항 생성</Link>

        </div>
    )
        ;
};

export default NoticeListPage;
