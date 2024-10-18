import React, { useState } from 'react';
import { Container, Row, Col, Modal, ModalHeader, ModalBody } from 'reactstrap';
import NotificationForm from './components/NotificationForm';
import LogHistory from './components/LogHistory';
import Login from './components/Login';
import Signup from './components/Signup';

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [showSignup, setShowSignup] = useState(false);
  const [successModalOpen, setSuccessModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [username, setUsername] = useState('');
  const [userId, setUserId] = useState(null);
  const categories = ['Email', 'Push', 'SMS'];
  const [logs, setLogs] = useState([]);

  const handleLogin = (username, encodedPassword) => {
    fetch('http://localhost:8080/api/users/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        username,
        encodedPassword,
      }),
    })
      .then(response => {
        if (response.ok) {
          setIsAuthenticated(true);
          setUsername(username);
          setModalMessage('Login successful!');
          setSuccessModalOpen(true);
          setTimeout(() => {
            setSuccessModalOpen(false);
          }, 5000);
        } else {
          throw new Error("Authentication failed");
        }
      })
      .catch(error => console.error('Login error:', error));
  };

  const handleSignup = (newUserId) => {
    setUserId(newUserId);
    setShowSignup(false);
    setModalMessage('Signup successful!');
    setSuccessModalOpen(true);
    setTimeout(() => {
      setSuccessModalOpen(false);
    }, 5000);
  };

  return (
    <Container>
      <Modal isOpen={successModalOpen}>
        <ModalHeader>Success</ModalHeader>
        <ModalBody>
          {modalMessage}
        </ModalBody>
      </Modal>

      <Row className="justify-content-center">
        <Col md={5} className="content-area">
          {!isAuthenticated ? (
            showSignup ? (
              <>
                <h1>Sign Up</h1>
                <Signup onSignup={handleSignup} toggle={() => setShowSignup(!showSignup)} />
              </>
            ) : (
              <>
                <h1>Login</h1>
                <Login onLogin={handleLogin} toggleSignup={() => setShowSignup(true)} />
              </>
            )
          ) : (
            <>
              <h1>Send Notification</h1>
              <NotificationForm categories={categories} username={username} userId={userId} setLogs={setLogs} />
              <br />
              <h2>Log History</h2>
              <LogHistory logs={logs} />
            </>
          )}
        </Col>
      </Row>
    </Container>
  );
}

export default App;
