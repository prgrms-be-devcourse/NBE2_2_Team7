import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate, useLocation } from 'react-router-dom';
import Header from './header/Header';
import BoardListPage from './board/BoardListPage';
import BoardDetailPage from './board/BoardDetailPage';
import CreateBoardPage from './board/CreateBoardPage';
import RegistrationForm from './member/RegistrationForm';
import LoginForm from './member/LoginForm';
import UpdateMemberForm from "./member/UpdateMemberForm";
import NoticeListPage from './notice/NoticeListPage';
import NoticeDetailPage from "./notice/NoticeDetailPage";
import CreateNoticePage from './notice/CreateNoticePage';
import ChatRoomList  from "./chat-room/ChatRoomList";
import CreateChatRoom from "./chat-room/CreateChatRoom";
import ChatRoomDetail from "./chat-room/ChatRoomDetail";


const App = () => {
    const [token, setToken] = useState(localStorage.getItem('token') || '');


    return (
        <Router>
            <AppContent token={token} setToken={setToken} /> {/* setToken 전달 */}
        </Router>
    );
};

const AppContent = ({ token, setToken }) => {
    const location = useLocation();
    const hideHeaderRoutes = ['/login', '/register'];

    return (
        <>
            {!hideHeaderRoutes.includes(location.pathname) && token && <Header />}
            <Routes>
                <Route path="/login" element={<LoginForm setToken={setToken} />} />
                <Route path="/register" element={<RegistrationForm />} />
                {token ? (
                    <>
                        <Route path="/" element={<BoardListPage />} />
                        <Route path="/board/:boardId" element={<BoardDetailPage />} />
                        <Route path="/create-board" element={<CreateBoardPage />} />
                        <Route path="/update-member" element={<UpdateMemberForm />} />

                        {/* 공지사항 라우트 */}
                        <Route path="/notices" element={<NoticeListPage />} />
                        <Route path="/notices/:id" element={<NoticeDetailPage />} />
                        <Route path="/create-notice" element={<CreateNoticePage />} />
                        <Route path="/edit-notice/:id" element={<CreateNoticePage />} />

                        {/* 채팅 기능 라우트 */}
                        <Route path="/chat-rooms/list" element={<ChatRoomList />} />
                        <Route path="/chat-room/create" element={<CreateChatRoom />} />
                        <Route path="/chat-room/:chatRoomId" element={<ChatRoomDetail />} />
                    </>
                ) : (
                    <Route path="*" element={<Navigate to="/login" />} />
                )}
            </Routes>
        </>
    );
};

export default App;
