import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Header from './header/Header'; // 새로 만든 Header 컴포넌트 추가
import BoardListPage from './board/BoardListPage';
import BoardDetailPage from './board/BoardDetailPage';
import CreateBoardPage from './board/CreateBoardPage';

const App = () => {
    return (
        <Router>
            <Header /> {/* 헤더 추가 */}
            <Routes>
                <Route path="/" element={<BoardListPage />} />
                <Route path="/board/:boardId" element={<BoardDetailPage />} />
                <Route path="/create-board" element={<CreateBoardPage />} />
            </Routes>
        </Router>
    );
};

export default App;
