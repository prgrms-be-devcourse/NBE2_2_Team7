// src/components/AdminMembersList.js
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../axios';
import { Box, Button, Typography, List, ListItem, ListItemText, Pagination } from '@mui/material';

const AdminMembersList = () => {
    const [members, setMembers] = useState([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        const fetchMembers = async () => {
            try {
                const response = await api.get('/admin/members', {
                    params: { page, size: 10 },
                });
                setMembers(response.data.content);
                setTotalPages(response.data.totalPages);
            } catch (error) {
                console.error('Error fetching members:', error);
            }
        };
        fetchMembers();
    }, [page]);

    return (
        <Box>
            <Typography variant="h4">Member List</Typography>
            <List>
                {members.map(member => (
                    <ListItem key={member.memberId}>
                        <ListItemText
                            primary={
                                <Link to={`/admin/member/${member.memberId}`} style={{ textDecoration: 'none', color: 'inherit' }}>
                                    {member.nickname} ({member.email})
                                </Link>
                            }
                        />
                    </ListItem>
                ))}
            </List>
            <Pagination
                count={totalPages}
                page={page}
                onChange={(event, value) => setPage(value)}
                color="primary"
            />
        </Box>
    );
};

export default AdminMembersList;
