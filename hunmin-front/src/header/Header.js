import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { AppBar, Toolbar, Badge, Typography, Grid, Button } from '@mui/material';
import NotificationsIcon from '@mui/icons-material/Notifications';

const Header = () => {
    const [notifications, setNotifications] = useState([]);
    const [showDropdown, setShowDropdown] = useState(false);
    const [showPopup, setShowPopup] = useState(false);
    const [popupMessage, setPopupMessage] = useState(null);

    const displayPopup = (message) => {
        if (message) {
            setPopupMessage(message);
            setShowPopup(true);
            setTimeout(() => setShowPopup(false), 5000);  // 5초 후 팝업 자동 닫힘
        }
    };

    const fetchNotifications = async () => {
        try {
            const response = await axios.get('http://localhost:8080/api/notification/2');
            const validNotifications = response.data.filter(notification => notification.message);
            setNotifications(validNotifications);
        } catch (error) {
            console.error('Error fetching notifications:', error);
        }
    };

    useEffect(() => {
        fetchNotifications();
        const eventSource = new EventSource('http://localhost:8080/api/notification/subscribe/2');

        eventSource.onmessage = (event) => {
            const newNotification = JSON.parse(event.data);
            setNotifications(prev => [newNotification, ...prev]);
            if (!newNotification.isRead) {
                displayPopup(newNotification.message);
            }
        };

        eventSource.onerror = () => {
            console.error('EventSource failed');
            eventSource.close();
        };

        return () => {
            eventSource.close();
        };
    }, []);

    const toggleDropdown = () => {
        // 드롭다운 열릴 때마다 최신 알림 상태를 불러옴
        if (!showDropdown) {
            fetchNotifications();
        }
        setShowDropdown(!showDropdown);
    };

    const markAsRead = async (notificationId) => {
        try {
            await axios.put(`http://localhost:8080/api/notification/${notificationId}`);
            setNotifications(prevNotifications =>
                prevNotifications.map(notification =>
                    notification.notificationId === notificationId
                        ? { ...notification, isRead: true }
                        : notification
                )
            );
        } catch (error) {
            console.error('Error marking notification as read:', error);
        }
    };

    const handleNotificationClick = (notification) => {
        markAsRead(notification.notificationId);
        window.location.href = notification.url;
    };

    return (
        <AppBar position="static">
            <Toolbar style={{ display: 'flex', justifyContent: 'space-between' }}>
                <Grid container justifyContent="space-between" alignItems="center">
                    <Grid item>
                        <Link to="/" style={{ marginRight: '20px', textDecoration: 'none', color: 'white' }}>
                            <Typography variant="h6">홈</Typography>
                        </Link>
                    </Grid>
                    <Grid item style={{ position: 'relative' }}>
                        <Badge badgeContent={notifications.filter(n => !n.isRead).length} color="secondary">
                            <NotificationsIcon onClick={toggleDropdown} style={{ cursor: 'pointer' }} />
                        </Badge>
                        {showDropdown && (
                            <div style={{
                                position: 'absolute',
                                top: '30px',
                                right: '0',
                                backgroundColor: '#fff',
                                border: '1px solid #ccc',
                                padding: '10px',
                                width: '300px',
                                zIndex: 9999
                            }}>
                                {notifications.length > 0 ? (
                                    <ul style={{ listStyle: 'none', padding: 0 }}>
                                        {notifications.map((notification) => (
                                            <li key={notification.notificationId} style={{
                                                marginBottom: '10px',
                                                padding: '10px',
                                                border: '1px solid #ccc',
                                                borderRadius: '5px',
                                                backgroundColor: notification.isRead ? '#e0e0e0' : '#fff',
                                                cursor: 'pointer'
                                            }} onClick={() => handleNotificationClick(notification)}>
                                                <div>
                                                    <strong style={{ color: 'black' }}>{notification.message}</strong>
                                                    <div style={{ marginTop: '5px' }}>
                                                        {notification.isRead ? (
                                                            <span style={{ color: 'black' }}>읽음</span>
                                                        ) : (
                                                            <Button
                                                                variant="outlined"
                                                                color="primary"
                                                                size="small"
                                                                onClick={(e) => {
                                                                    e.stopPropagation();
                                                                    markAsRead(notification.notificationId);
                                                                }}
                                                            >
                                                                확인
                                                            </Button>
                                                        )}
                                                    </div>
                                                </div>
                                            </li>
                                        ))}
                                    </ul>
                                ) : (
                                    <p>새로운 알림이 없습니다.</p>
                                )}
                            </div>
                        )}
                    </Grid>
                </Grid>
            </Toolbar>
            {showPopup && popupMessage && (
                <div style={{
                    position: 'fixed',
                    top: '20px',
                    right: '50px',
                    backgroundColor: 'white',
                    padding: '10px',
                    borderRadius: '5px',
                    boxShadow: '0 0 10px rgba(0, 0, 0, 0.2)',
                    color: 'black',
                    zIndex: 9999
                }}>
                    <p>{popupMessage}</p>
                </div>
            )}
        </AppBar>
    );
};

export default Header;
