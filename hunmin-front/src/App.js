import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import BoardListPage from './board/BoardListPage';
import BoardDetailPage from './board/BoardDetailPage';
import CreateBoardPage from './board/CreateBoardPage';

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<BoardListPage />} />
                <Route path="/board/:boardId" element={<BoardDetailPage />} />
                <Route path="/create-board" element={<CreateBoardPage />} />
            </Routes>
        </Router>
    );
};

export default App;
