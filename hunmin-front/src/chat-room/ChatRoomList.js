import React, { useState, useEffect } from 'react';
import api from '../axios'; // API 요청을 위해 정의된 axios 인스턴스
import {
    Box,
    Card,
    CardHeader,
    CardContent,
    CardActions,
    Typography,
    IconButton,
    Menu,
    MenuItem,
    List,
    ListItem,
    Divider,
    Snackbar,
    Alert,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    TextField,
} from '@mui/material';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import DeleteIcon from '@mui/icons-material/Delete';

const ChatRoomCard = ({ room, onEnter, onRightClick }) => {
    return (
        <Card
            sx={{ marginBottom: 2, cursor: 'pointer', width: '100%' }}
            onClick={() => onEnter(room.chatRoomId)}
            onContextMenu={(e) => onRightClick(e, room.chatRoomId)}
        >
            <CardHeader
                title={`대화방 (${room.nickName}, ${room.partnerName || 'N/A'})`}
                action={
                    <IconButton onClick={(e) => onRightClick(e, room.chatRoomId)}>
                        <MoreVertIcon />
                    </IconButton>
                }
            />
            <CardContent>
                <Typography variant="body2" color="text.secondary">
                    생성일: {room.createdAt ? new Date(room.createdAt).toLocaleString() : 'N/A'}
                </Typography>
            </CardContent>
        </Card>
    );
};

const ChatRoomList = () => {
    const [chatRooms, setChatRooms] = useState([]);
    const [anchorEl, setAnchorEl] = useState(null);
    const [selectedChatRoom, setSelectedChatRoom] = useState(null);
    const [snackbar, setSnackbar] = useState({ open: false, message: '', severity: 'success' });
    const [openDeleteDialog, setOpenDeleteDialog] = useState(false);  // 삭제 Dialog 상태 추가
    const [openCreateDialog, setOpenCreateDialog] = useState(false);  // 생성 Dialog 상태 추가
    const [newNickName, setNewNickName] = useState('');
    const [partnerName, setPartnerName] = useState('');  // 파트너 이름 상태 추가

    useEffect(() => {
        // 인증된 사용자인지 확인 후 요청
        api.get('/chat-room/list')
            .then(response => {
                setChatRooms(response.data);
            })
            .catch(error => {
                setSnackbar({
                    open: true,
                    message: '채팅방 목록을 불러오는데 실패했습니다.',
                    severity: 'error',
                });
            });
    }, []);

    const enterRoom = (chatRoomId) => {
        window.location.href = `/chat-room/${chatRoomId}`;
    };

    const handleMenuOpen = (event, chatRoomId) => {
        event.stopPropagation(); // onClick 트리거 방지
        setAnchorEl(event.currentTarget);
        setSelectedChatRoom(chatRoomId);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
        setSelectedChatRoom(null);
    };

    // 삭제 버튼 클릭 시 파트너 이름 입력 Dialog 열기
    const openPartnerNameDialog = () => {
        setOpenDeleteDialog(true);  // 삭제 Dialog 열기
    };

    // 채팅방 삭제 요청
    const deleteChatRoom = () => {
        if (!partnerName) {
            setSnackbar({
                open: true,
                message: '대화상대 닉네임을 입력해주세요.',
                severity: 'warning',
            });
            return;
        }
        if (selectedChatRoom) {
            api.delete(`/chat-room/${selectedChatRoom}/${partnerName}`)
                .then(() => {
                    setChatRooms(chatRooms.filter(room => room.chatRoomId !== selectedChatRoom));
                    setSnackbar({
                        open: true,
                        message: '채팅방이 성공적으로 삭제되었습니다.',
                        severity: 'success',
                    });
                    handleCloseDeleteDialog(); // 다이얼로그 닫기
                })
                .catch(error => {
                    setSnackbar({
                        open: true,
                        message: '닉네임이 일치하지 않습니다.',
                        severity: 'error',
                    });
                });
            handleMenuClose();
        }
    };

    const handleCloseSnackbar = () => {
        setSnackbar({ ...snackbar, open: false });
    };

    // 삭제 다이얼로그 닫기
    const handleCloseDeleteDialog = () => {
        setOpenDeleteDialog(false);
        setPartnerName('');  // 입력 필드 초기화
    };

    // 생성 다이얼로그 닫기
    const handleCloseCreateDialog = () => {
        setOpenCreateDialog(false);
        setNewNickName('');  // 입력 필드 초기화
    };

    const handleCreateChatRoom = () => {
        if (newNickName.trim() === '') {
            setSnackbar({
                open: true,
                message: '닉네임을 입력해주세요.',
                severity: 'warning',
            });
            return;
        }

        api.post(`/chat-room/${newNickName}`)
            .then(response => {
                setChatRooms([...chatRooms, response.data]);
                setSnackbar({
                    open: true,
                    message: '채팅방이 성공적으로 생성되었습니다.',
                    severity: 'success',
                });
                handleCloseCreateDialog();
            })
            .catch(error => {
                setSnackbar({
                    open: true,
                    message: '채팅방 생성에 실패했습니다.',
                    severity: 'error',
                });
            });
    };

    return (
        <Box sx={{ maxWidth: 700, margin: 'auto', padding: 2 }}>
            <Typography variant="h4" gutterBottom>
                채팅방 목록
            </Typography>
            <Button variant="contained" color="primary" onClick={() => setOpenCreateDialog(true)} sx={{ marginBottom: 2 }}>
                채팅방 생성
            </Button>
            <List sx={{ width: '100%' }}>
                {chatRooms.map(room => (
                    <React.Fragment key={room.chatRoomId}> {/* chatRoomId가 고유한지 확인 */}
                        <ListItem>
                            <ChatRoomCard
                                room={room}
                                onEnter={enterRoom}
                                onRightClick={handleMenuOpen}
                            />
                        </ListItem>
                        <Divider component="li" />
                    </React.Fragment>
                ))}
            </List>
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                <MenuItem onClick={openPartnerNameDialog}>
                    <DeleteIcon fontSize="small" sx={{ marginRight: 1 }} />
                    채팅방 삭제
                </MenuItem>
            </Menu>

            {/* Snackbar */}
            <Snackbar
                open={snackbar.open}
                autoHideDuration={6000}
                onClose={handleCloseSnackbar}
                anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
            >
                <Alert onClose={handleCloseSnackbar} severity={snackbar.severity} sx={{ width: '100%' }}>
                    {snackbar.message}
                </Alert>
            </Snackbar>

            {/* 삭제 다이얼로그 */}
            <Dialog open={openDeleteDialog} onClose={handleCloseDeleteDialog}>
                <DialogTitle>대화상대 닉네임 입력</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        채팅방 삭제를 위해 대화상대 닉네임을 입력해주세요.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="파트너 이름"
                        type="text"
                        fullWidth
                        variant="standard"
                        value={partnerName}
                        onChange={(e) => setPartnerName(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseDeleteDialog}>취소</Button>
                    <Button onClick={deleteChatRoom}>삭제</Button>
                </DialogActions>
            </Dialog>

            {/* 채팅방 생성 다이얼로그 */}
            <Dialog open={openCreateDialog} onClose={handleCloseCreateDialog}>
                <DialogTitle>채팅방 생성</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        대화상대 닉네임을 입력해주세요.
                    </DialogContentText>
                    <TextField
                        autoFocus
                        margin="dense"
                        label="닉네임"
                        type="text"
                        fullWidth
                        variant="standard"
                        value={newNickName}
                        onChange={(e) => setNewNickName(e.target.value)}
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleCloseCreateDialog}>취소</Button>
                    <Button onClick={handleCreateChatRoom}>생성</Button>
                </DialogActions>
            </Dialog>
        </Box>
    );
};

export default ChatRoomList;
