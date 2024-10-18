
import React, { useState } from 'react';
import { Form, FormGroup, Label, Input, Button } from 'reactstrap';

const Login = ({ onLogin, toggleSignup }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    const encodedPassword = btoa(password);
    onLogin(username, encodedPassword);
  };

  return (
    <>
      <Form onSubmit={handleSubmit}>
        <FormGroup>
          <Label style={{ fontWeight: 'bold' }} for="username">Username</Label>
          <Input 
            type="text" 
            id="username" 
            value={username} 
            onChange={(e) => setUsername(e.target.value)} 
            required 
          />
        </FormGroup>
        <FormGroup>
          <Label style={{ fontWeight: 'bold' }} for="password">Password</Label>
          <Input 
            type="password" 
            id="password" 
            value={password} 
            onChange={(e) => setPassword(e.target.value)} 
            required 
          />
        </FormGroup>
        <Button type="submit" color="primary">Login</Button>
      </Form>
      <p style={{ color: 'blue', cursor: 'pointer' }} onClick={toggleSignup}>
        Create an account
      </p>
    </>
  );
};

export default Login;
