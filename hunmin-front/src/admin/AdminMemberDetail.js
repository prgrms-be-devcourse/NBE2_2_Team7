// AdminMemberDetail.js
import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../axios';
import {
    Box,
    Typography,
    Button,
    Paper,
    CircularProgress,
    Grid,
    Divider
} from '@mui/material';

const AdminMemberDetail = () => {
    const { memberId } = useParams();
    const [memberInfo, setMemberInfo] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchMemberInfo = async () => {
            setLoading(true);
            try {
                const response = await api.get(`/admin/member/${memberId}`);
                setMemberInfo(response.data);
            } catch (error) {
                console.error('Error fetching member info:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchMemberInfo();
    }, [memberId]);

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Box sx={{ maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
            <Paper sx={{ p: 3 }}>
                <Typography variant="h4" sx={{ mb: 3 }}>회원 상세 정보</Typography>
                {memberInfo && (
                    <Grid container spacing={2}>
                        <Grid item xs={12}>
                            <Typography variant="h6">기본 정보</Typography>
                            <Divider sx={{ my: 1 }} />
                            <Typography>회원 ID: {memberInfo.memberId}</Typography>
                            <Typography>이메일: {memberInfo.email}</Typography>
                            <Typography>닉네임: {memberInfo.nickname}</Typography>
                        </Grid>
                        <Grid item xs={12} sx={{ mt: 2 }}>
                            <Typography variant="h6">활동 정보</Typography>
                            <Divider sx={{ my: 1 }} />
                            <Typography>작성 게시글: {memberInfo.boardCount}개</Typography>
                            <Typography>작성 댓글: {memberInfo.commentCount}개</Typography>
                        </Grid>
                        <Grid item xs={12} sx={{ mt: 3 }}>
                            <Button
                                component={Link}
                                to={`/admin/member/${memberId}/posts-comments`}
                                variant="contained"
                                color="primary"
                                fullWidth
                            >
                                게시글 및 댓글 보기
                            </Button>
                        </Grid>
                    </Grid>
                )}
            </Paper>
        </Box>
    );
};

export default AdminMemberDetail;

// // src/components/AdminMemberDetail.js
// import React, { useEffect, useState } from 'react';
// import { useParams, Link } from 'react-router-dom';
// import api from '../axios';
// import { Box, Typography, Button } from '@mui/material';
//
// const AdminMemberDetail = () => {
//     const { memberId } = useParams();
//     const [memberInfo, setMemberInfo] = useState(null);
//
//     useEffect(() => {
//         const fetchMemberInfo = async () => {
//             try {
//                 const response = await api.get(`/admin/member/${memberId}`);
//                 setMemberInfo(response.data);
//             } catch (error) {
//                 console.error('Error fetching member info:', error);
//             }
//         };
//
//         fetchMemberInfo();
//     }, [memberId]);
//
//     if (!memberInfo) return <div>Loading...</div>;
//
//     return (
//         <Box>
//             <Typography variant="h4">Member Information</Typography>
//             <Typography>ID: {memberInfo.memberId}</Typography>
//             <Typography>Email: {memberInfo.email}</Typography>
//             <Typography>Nickname: {memberInfo.nickname}</Typography>
//             <Typography>Boards: {memberInfo.boardCount}</Typography>
//             <Typography>Comments: {memberInfo.commentCount}</Typography>
//             <Link to={`/admin/member/${memberId}/posts-comments`}>
//                 <Button variant="contained" color="primary">View Posts and Comments</Button>
//             </Link>
//         </Box>
//     );
// };
//
// export default AdminMemberDetail;
