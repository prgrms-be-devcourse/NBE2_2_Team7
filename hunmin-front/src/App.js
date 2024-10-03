import React, { useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate, useLocation } from 'react-router-dom';
import Header from './header/Header';
import BoardListPage from './board/BoardListPage';
import BoardDetailPage from './board/BoardDetailPage';
import CreateBoardPage from './board/CreateBoardPage';
import RegistrationForm from './member/RegistrationForm';
import LoginForm from './member/LoginForm';
import UpdateMemberForm from "./member/UpdateMemberForm";

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
                    </>
                ) : (
                    <Route path="*" element={<Navigate to="/login" />} />
                )}
            </Routes>
        </>
    );
};

export default App;
