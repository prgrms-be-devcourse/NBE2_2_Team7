import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../axios';
import {
    Box,
    Typography,
    List,
    ListItem,
    ListItemText,
    Pagination,
    Paper,
    Divider,
    CircularProgress
} from '@mui/material';

const AdminMemberPostsAndComments = () => {
    const { memberId } = useParams();
    const [posts, setPosts] = useState({ content: [], totalPages: 0 });
    const [comments, setComments] = useState({ content: [], totalPages: 0 });
    const [postsPage, setPostsPage] = useState(1);
    const [commentsPage, setCommentsPage] = useState(1);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchPosts(postsPage);
        fetchComments(commentsPage);
    }, [memberId, postsPage, commentsPage]);

    const fetchPosts = async (page) => {
        setLoading(true);
        try {
            const response = await api.get(`/admin/member/${memberId}/boards`, {
                params: { page }
            });
            console.log('Posts Response:', response.data); // 디버깅용 로그
            setPosts({
                content: response.data.content || [],
                totalPages: response.data.totalPages || 0
            });
        } catch (error) {
            console.error('Error fetching posts:', error);
            setPosts({ content: [], totalPages: 0 });
        } finally {
            setLoading(false);
        }
    };

    const fetchComments = async (page) => {
        setLoading(true);
        try {
            const response = await api.get(`/admin/member/${memberId}/comments`, {
                params: { page }
            });
            console.log('Comments Response:', response.data); // 디버깅용 로그
            setComments({
                content: response.data.content || [],
                totalPages: response.data.totalPages || 0
            });
        } catch (error) {
            console.error('Error fetching comments:', error);
            setComments({ content: [], totalPages: 0 });
        } finally {
            setLoading(false);
        }
    };

    const handlePostsPageChange = (event, newPage) => {
        setPostsPage(newPage);
    };

    const handleCommentsPageChange = (event, newPage) => {
        setCommentsPage(newPage);
    };

    const renderPosts = () => {
        return posts.content.map(post => (
            <ListItem key={post.id} divider>
                <ListItemText
                    primary={
                        <Box>
                            <Typography variant="subtitle1" component="span" sx={{ fontWeight: 'bold' }}>
                                {post.title}
                            </Typography>
                            {post.noticeYn && (
                                <Typography
                                    component="span"
                                    sx={{
                                        ml: 1,
                                        color: 'error.main',
                                        fontSize: '0.8rem',
                                        fontWeight: 'bold'
                                    }}
                                >
                                    [공지]
                                </Typography>
                            )}
                        </Box>
                    }
                    secondary={
                        <React.Fragment>
                            <Typography variant="body2" color="text.secondary">
                                작성자: {post.nickname || '알 수 없음'} |
                                작성일: {new Date(post.createdAt).toLocaleString()} |
                                조회수: {post.viewCount}
                            </Typography>
                            <Typography variant="body2" sx={{ mt: 1 }}>
                                {post.content?.substring(0, 100)}
                                {post.content?.length > 100 ? '...' : ''}
                            </Typography>
                        </React.Fragment>
                    }
                />
            </ListItem>
        ));
    };

    const renderComments = () => {
        return comments.content.map(comment => (
            <ListItem key={comment.id} divider>
                <ListItemText
                    primary={
                        <Typography component="div" sx={{ wordBreak: 'break-word' }}>
                            {comment.content}
                        </Typography>
                    }
                    secondary={
                        <React.Fragment>
                            <Typography variant="body2" color="text.secondary">
                                작성자: {comment.nickname || '알 수 없음'} |
                                작성일: {new Date(comment.createdAt).toLocaleString()}
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                원글: {comment.boardTitle || '삭제된 게시글'}
                            </Typography>
                        </React.Fragment>
                    }
                />
            </ListItem>
        ));
    };

    if (loading) {
        return (
            <Box display="flex" justifyContent="center" alignItems="center" minHeight="200px">
                <CircularProgress />
            </Box>
        );
    }

    return (
        <Box sx={{ maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
            <Paper sx={{ mb: 4, p: 2 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                    <Typography variant="h5">게시글 목록</Typography>
                    <Typography variant="body2" color="text.secondary">
                        총 {posts.totalElements || 0}개의 게시글
                    </Typography>
                </Box>
                <Divider sx={{ mb: 2 }} />
                <List>
                    {posts.content.length > 0 ? renderPosts() :
                        <ListItem>
                            <ListItemText primary="작성한 게시글이 없습니다." />
                        </ListItem>
                    }
                </List>
                {posts.totalPages > 1 && (
                    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
                        <Pagination
                            count={posts.totalPages}
                            page={postsPage}
                            onChange={handlePostsPageChange}
                            color="primary"
                        />
                    </Box>
                )}
            </Paper>

            <Paper sx={{ p: 2 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                    <Typography variant="h5">댓글 목록</Typography>
                    <Typography variant="body2" color="text.secondary">
                        총 {comments.totalElements || 0}개의 댓글
                    </Typography>
                </Box>
                <Divider sx={{ mb: 2 }} />
                <List>
                    {comments.content.length > 0 ? renderComments() :
                        <ListItem>
                            <ListItemText primary="작성한 댓글이 없습니다." />
                        </ListItem>
                    }
                </List>
                {comments.totalPages > 1 && (
                    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
                        <Pagination
                            count={comments.totalPages}
                            page={commentsPage}
                            onChange={handleCommentsPageChange}
                            color="primary"
                        />
                    </Box>
                )}
            </Paper>
        </Box>
    );
};

export default AdminMemberPostsAndComments;

// import React, { useEffect, useState } from 'react';
// import { useParams } from 'react-router-dom';
// import api from '../axios';
// import {
//     Box,
//     Typography,
//     List,
//     ListItem,
//     ListItemText,
//     Pagination,
//     Paper,
//     Divider
// } from '@mui/material';
//
// const AdminMemberPostsAndComments = () => {
//     const { memberId } = useParams();
//     const [posts, setPosts] = useState({ content: [], totalPages: 0 });
//     const [comments, setComments] = useState({ content: [], totalPages: 0 });
//     const [postsPage, setPostsPage] = useState(1);
//     const [commentsPage, setCommentsPage] = useState(1);
//
//     useEffect(() => {
//         fetchPosts(postsPage);
//         fetchComments(commentsPage);
//     }, [memberId, postsPage, commentsPage]);
//
//     const fetchPosts = async (page) => {
//         try {
//             const response = await api.get(`/admin/member/${memberId}/boards`, {
//                 params: { page }
//             });
//             setPosts({
//                 content: response.data.content || [],
//                 totalPages: response.data.totalPages || 0
//             });
//         } catch (error) {
//             console.error('Error fetching posts:', error);
//             setPosts({ content: [], totalPages: 0 });
//         }
//     };
//
//     const fetchComments = async (page) => {
//         try {
//             const response = await api.get(`/admin/member/${memberId}/comments`, {
//                 params: { page }
//             });
//             setComments({
//                 content: response.data.content || [],
//                 totalPages: response.data.totalPages || 0
//             });
//         } catch (error) {
//             console.error('Error fetching comments:', error);
//             setComments({ content: [], totalPages: 0 });
//         }
//     };
//
//     const handlePostsPageChange = (event, newPage) => {
//         setPostsPage(newPage);
//     };
//
//     const handleCommentsPageChange = (event, newPage) => {
//         setCommentsPage(newPage);
//     };
//
//     const renderPosts = () => {
//         return posts.content.map(post => (
//             <ListItem key={post.id} divider>
//                 <ListItemText
//                     primary={post.title}
//                     secondary={
//                         <React.Fragment>
//                             <Typography variant="body2" color="text.secondary">
//                                 작성일: {new Date(post.createdAt).toLocaleDateString()}
//                             </Typography>
//                             <Typography variant="body2">
//                                 {post.content?.substring(0, 100)}...
//                             </Typography>
//                         </React.Fragment>
//                     }
//                 />
//             </ListItem>
//         ));
//     };
//
//     const renderComments = () => {
//         return comments.content.map(comment => (
//             <ListItem key={comment.id} divider>
//                 <ListItemText
//                     primary={comment.content}
//                     secondary={
//                         <Typography variant="body2" color="text.secondary">
//                             작성일: {new Date(comment.createdAt).toLocaleDateString()}
//                         </Typography>
//                     }
//                 />
//             </ListItem>
//         ));
//     };
//
//     return (
//         <Box sx={{ maxWidth: '800px', margin: '0 auto', padding: '20px' }}>
//             <Paper sx={{ mb: 4, p: 2 }}>
//                 <Typography variant="h5" gutterBottom>게시글 목록</Typography>
//                 <List>
//                     {posts.content.length > 0 ? renderPosts() :
//                         <ListItem>
//                             <ListItemText primary="작성한 게시글이 없습니다." />
//                         </ListItem>
//                     }
//                 </List>
//                 {posts.totalPages > 1 && (
//                     <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
//                         <Pagination
//                             count={posts.totalPages}
//                             page={postsPage}
//                             onChange={handlePostsPageChange}
//                             color="primary"
//                         />
//                     </Box>
//                 )}
//             </Paper>
//
//             <Paper sx={{ p: 2 }}>
//                 <Typography variant="h5" gutterBottom>댓글 목록</Typography>
//                 <List>
//                     {comments.content.length > 0 ? renderComments() :
//                         <ListItem>
//                             <ListItemText primary="작성한 댓글이 없습니다." />
//                         </ListItem>
//                     }
//                 </List>
//                 {comments.totalPages > 1 && (
//                     <Box sx={{ display: 'flex', justifyContent: 'center', mt: 2 }}>
//                         <Pagination
//                             count={comments.totalPages}
//                             page={commentsPage}
//                             onChange={handleCommentsPageChange}
//                             color="primary"
//                         />
//                     </Box>
//                 )}
//             </Paper>
//         </Box>
//     );
// };
//
// export default AdminMemberPostsAndComments;