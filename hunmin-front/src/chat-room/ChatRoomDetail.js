import React, { useState, useEffect, useRef } from 'react';
import {
    AppBar,
    Toolbar,
    IconButton,
    Typography,
    Box,
    TextField,
    Button,
    Container,
    List,
    ListItem,
    Card,
    CardContent
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import SendIcon from '@mui/icons-material/Send';
import { useParams, useNavigate } from 'react-router-dom';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import api from '../axios';

const ChatRoomDetail = () => {
    const { chatRoomId } = useParams();
    const navigate = useNavigate();
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [roomName, setRoomName] = useState('');
    const [isConnected, setIsConnected] = useState(false);
    const ws = useRef(null);
    const chatInputRef = useRef(null);

    useEffect(() => {
        // 채팅방 세부 정보 및 이전 메시지 로드
        const fetchRoomDetails = async () => {
            try {
                const response = await api.get(`/chat-room/${chatRoomId}`);
                setRoomName(response.data.nickname);
            } catch (error) {
                console.error('Failed to load chat room details:', error);
            }

            try {
                const response = await api.get(`/chat/messages/${chatRoomId}`);
                setMessages(response.data);
            } catch (error) {
                console.error('Failed to load chat messages:', error);
            }
        };

        fetchRoomDetails();

        // SockJS 및 STOMP 초기화
        const sock = new SockJS('/ws-stomp');
        ws.current = Stomp.over(sock);
        const token = localStorage.getItem('wschat.token');

        ws.current.configure({
            reconnectDelay: 5000, // 재연결 지연 시간 (5초)
            heartbeatIncoming: 4000, // 서버로부터의 Heartbeat 체크
            heartbeatOutgoing: 4000, // 클라이언트로부터의 Heartbeat 체크
            onConnect: () => {
                console.log('WebSocket connected');
                setIsConnected(true);
                ws.current.subscribe(`/sub/chat/room/${chatRoomId}`, (message) => {
                    const recv = JSON.parse(message.body);
                    setMessages((prevMessages) => [recv, ...prevMessages]);
                });
            },
            onDisconnect: () => {
                console.log('WebSocket disconnected');
                setIsConnected(false);
            },
            onStompError: (frame) => {
                console.error('Broker reported error: ', frame.headers['message']);
                console.error('Additional details: ', frame.body);
                alert('서버 연결에 실패 하였습니다. 다시 접속해 주십시요.');
                navigate('/');
            },
            connectHeaders: {
                Authorization: `Bearer ${token}`
            }
        });

        // WebSocket 연결 활성화
        ws.current.activate();

        // 컴포넌트 언마운트 시 WebSocket 연결 종료
        return () => {
            if (ws.current) {
                ws.current.deactivate(() => {
                    console.log('WebSocket disconnected');
                });
            }
        };
    }, [chatRoomId, navigate]);

    // 메시지 전송 함수
    const sendMessage = () => {
        if (newMessage.trim() == '' || !isConnected) return;

        const messageData = {
            type: 'TALK',
            chatRoomId: chatRoomId,
            message: newMessage,
            memberId: roomName,
        };

        ws.current.publish({
            destination: '/pub/api/chat/message',
            body: JSON.stringify(messageData),
        });

        setNewMessage('');
        chatInputRef.current.focus();
    };

    // 메시지 삭제 함수
    const deleteMessage = (index) => {
        const message = messages[index];
        if (!message || message.memberId !== roomName) {
            alert('본인의 메시지만 삭제할 수 있습니다.');
            return;
        }

        api.delete(`/chat/${message.chatMessageId}`)
            .then(() => {
                setMessages((prevMessages) => prevMessages.filter((_, i) => i !== index));
            })
            .catch((error) => {
                console.error('메시지 삭제에 실패하였습니다.', error);
                alert('메세지 삭제에 실패하였습니다.');
            });
    };

    return (
        <Container maxWidth="sm">
            {/* 상단 앱바: 방 제목과 뒤로가기 버튼 */}
            <AppBar position="static">
                <Toolbar>
                    <IconButton edge="start" color="inherit" onClick={() => navigate(-1)}>
                        <ArrowBackIcon />
                    </IconButton>
                    <Typography variant="h6" component="div">
                        {roomName}
                    </Typography>
                </Toolbar>
            </AppBar>

            {/* 채팅 메시지 리스트 */}
            <Box mt={2} mb={2} sx={{ height: '60vh', overflowY: 'auto', display: 'flex', flexDirection: 'column-reverse' }}>
                <List>
                    {messages.map((message, index) => (
                        <ListItem key={message.chatMessageId || index}>
                            <Card sx={{ width: '100%', backgroundColor: message.memberId == roomName ? '#dcf8c6' : '#f1f0f0' }}>
                                <CardContent>
                                    <Typography variant="body1">
                                        <strong>{message.memberId == roomName ? '나' : '상대방'}</strong>: {message.message}
                                        {message.memberId == roomName && (
                                            <Button
                                                color="error"
                                                size="small"
                                                onClick={() => deleteMessage(index)}
                                                sx={{ float: 'right' }}
                                            >
                                                삭제
                                            </Button>
                                        )}
                                    </Typography>
                                </CardContent>
                            </Card>
                        </ListItem>
                    ))}
                </List>
            </Box>

            {/* 입력 필드 및 전송 버튼 */}
            <Box display="flex" mt={2} mb={4}>
                <TextField
                    fullWidth
                    variant="outlined"
                    label="메시지를 입력하세요"
                    value={newMessage}
                    onChange={(e) => setNewMessage(e.target.value)}
                    onKeyPress={(e) => {
                        if (e.key === 'Enter') sendMessage();
                    }}
                    inputRef={chatInputRef}
                />
                <Button
                    variant="contained"
                    color="primary"
                    endIcon={<SendIcon />}
                    onClick={sendMessage}
                    sx={{ ml: 1 }}
                >
                    전송
                </Button>
            </Box>
        </Container>
    );
};

export default ChatRoomDetail;
