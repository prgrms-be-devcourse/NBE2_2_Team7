import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './header/Header'; // 새로 만든 Header 컴포넌트 추가
import BoardListPage from './board/BoardListPage';
import BoardDetailPage from './board/BoardDetailPage';
import CreateBoardPage from './board/CreateBoardPage';
import NoticeListPage from './notice/NoticeListPage';
import NoticeDetailPage from './notice/NoticeDetailPage';
import CreateNoticePage from './notice/CreateNoticePage';

const App = () => {
    return (
        <Router>
            <Header /> {/* 헤더 추가 */}
            <Routes>
                {/* 게시판 라우트 */}
                <Route path="/" element={<BoardListPage />} />
                <Route path="/board/:boardId" element={<BoardDetailPage />} />
                <Route path="/create-board" element={<CreateBoardPage />} />

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
