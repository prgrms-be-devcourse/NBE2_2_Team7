// src/components/AdminMemberDetail.js
import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../axios';
import { Box, Typography, Button } from '@mui/material';

const AdminMemberDetail = () => {
    const { memberId } = useParams();
    const [memberInfo, setMemberInfo] = useState(null);

    useEffect(() => {
        const fetchMemberInfo = async () => {
            try {
                const response = await api.get(`/admin/member/${memberId}`);
                setMemberInfo(response.data);
            } catch (error) {
                console.error('Error fetching member info:', error);
            }
        };

        fetchMemberInfo();
    }, [memberId]);

    if (!memberInfo) return <div>Loading...</div>;

    return (
        <Box>
            <Typography variant="h4">Member Information</Typography>
            <Typography>ID: {memberInfo.memberId}</Typography>
            <Typography>Email: {memberInfo.email}</Typography>
            <Typography>Nickname: {memberInfo.nickname}</Typography>
            <Typography>Boards: {memberInfo.boardCount}</Typography>
            <Typography>Comments: {memberInfo.commentCount}</Typography>
            <Link to={`/admin/member/${memberId}/posts-comments`}>
                <Button variant="contained" color="primary">View Posts and Comments</Button>
            </Link>
        </Box>
    );
};

export default AdminMemberDetail;
