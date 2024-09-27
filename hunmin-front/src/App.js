import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import NavBar from './components/NavBar';
import BoardListPage from './board/BoardListPage';
import BoardDetailPage from './board/BoardDetailPage';
import CreateBoardPage from './board/CreateBoardPage';
import RegistrationForm from './member/RegistrationForm';
import UpdateMemberForm from './member/UpdateMemberForm';
import MainComponent from './member/MainComponent';
import AdminComponent from './member/AdminComponent';
import LoginForm from './member/LoginForm';
import axios from 'axios';
import NoticeListPage from './notice/NoticeListPage';
import NoticeDetailPage from './notice/NoticeDetailPage';
import CreateNoticePage from './notice/CreateNoticePage';

const App = () => {
    const [token, setToken] = useState('');

    const handleLogin = (token) => {
        setToken(token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    };

    return (
        <Router>
            <NavBar />
            <Routes>
                {/* 게시판 라우트 */}
                <Route path="/" element={<BoardListPage />} />
                <Route path="/board/:boardId" element={<BoardDetailPage />} />
                <Route path="/create-board" element={<CreateBoardPage />} />

                <Route path="/register" element={<RegistrationForm />} />
                <Route path="/login" element={<LoginForm onLogin={handleLogin} />} />
                <Route path="/update" element={<UpdateMemberForm />} />
                <Route path="/main" element={<MainComponent />} />
                <Route path="/admin" element={<AdminComponent />} />

                {/* 공지사항 라우트 */}
                <Route path="/notices" element={<NoticeListPage />} />
                <Route path="/notices/:id" element={<NoticeDetailPage />} />
                <Route path="/create-notice" element={<CreateNoticePage />} />
                <Route path="/edit-notice/:id" element={<CreateNoticePage />} />

            </Routes>
        </Router>

    );
};

export default App;
