import React, { useState, useEffect } from 'react';
import { Form, FormGroup, Label, Input, Button, Modal, ModalHeader, ModalBody, ModalFooter, Container, Row } from 'reactstrap';
import Multiselect from './Multiselect';

const NotificationForm = ({ username, userId, setLogs }) => {
  const [category, setCategory] = useState('');
  const [message, setMessage] = useState('');
  const [modal, setModal] = useState(false);
  const [modalMessage, setModalMessage] = useState('');
  const [successModal, setSuccessModal] = useState(false);
  const [subscribedCategories, setSubscribedCategories] = useState([]);
  const [selectedCategories, setSelectedCategories] = useState([]);
  const [notificationChannels, setNotificationChannels] = useState([]);

  const toggleModal = () => setModal(!modal);
  const toggleSuccessModal = () => setSuccessModal(!successModal);

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!message.trim()) {
      setModalMessage("The message field can't be empty. Please fill in the message field.");
      toggleModal();
      return;
    }

    if (selectedCategories.length === 0) {
      setModalMessage("Please select at least one category.");
      toggleModal();
      return;
    }

    const notification = {
      userId: userId,
      message: message,
      channel: category,
      timestamp: new Date().toISOString(),
      delivered: true,
      subscribedCategories: selectedCategories
    };

    fetch('http://localhost:8080/api/notifications', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(notification),
    })
      .then(response => {
        if (response.ok) {
          return response.json();
        }
        throw new Error('Network response was not ok.');
      })
      .then(data => {
        console.log('Notification sent successfully:', data);
        setMessage('');
        setSelectedCategories([]);
        fetchLogs(); 
        toggleSuccessModal(); 
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  };

  const fetchLogs = () => {
    fetch(`http://localhost:8080/api/notifications/${username}`, {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    })
      .then(response => response.json())
      .then(data => {
        setLogs(data);
      })
      .catch(error => console.error('Error fetching logs:', error));
  };

  const handleChange = (name, value) => {
    setSelectedCategories(value);
  };

  useEffect(() => {
    fetch(`http://localhost:8080/api/users/${username}/subscribed-categories`)
      .then(response => response.json())
      .then(data => setSubscribedCategories(data))
      .catch(error => console.error('Error fetching subscribed categories:', error));

    fetch(`http://localhost:8080/api/users/${username}/notification-channels`)
      .then(response => response.json())
      .then(data => {
        setNotificationChannels(data);
        setCategory(data[0] || '');
      })
      .catch(error => console.error('Error fetching notification channels:', error));
  }, [username]);

  return (
    <Container>
      <Row className="justify-content-center">
        <Form onSubmit={handleSubmit}>
          <FormGroup>
            <Label style={{ fontWeight: 'bold' }} for="category">Channel:</Label>
            <Input type="select" value={category} onChange={(e) => setCategory(e.target.value)}>
              {notificationChannels.map((channel, index) => (
                <option key={index} value={channel}>{channel}</option>
              ))}
            </Input>
          </FormGroup>
          <FormGroup>
            <Label style={{ fontWeight: 'bold' }} for="message">Message</Label>
            <Input 
              type="textarea" 
              value={message} 
              onChange={(e) => setMessage(e.target.value)} 
              required
            />
          </FormGroup>
          <Multiselect
            label="Subscribed Categories:"
            name="subscribedCategories"
            options={subscribedCategories}
            selectedOptions={selectedCategories}
            onChange={handleChange}
          />
          <Button type="submit" color="primary" className="w-100">Send Notification</Button>
        </Form>
      </Row>

      <Modal isOpen={modal} toggle={toggleModal}>
        <ModalHeader toggle={toggleModal}>Warning</ModalHeader>
        <ModalBody>
          {modalMessage}
          </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleModal}>Close</Button>
        </ModalFooter>
      </Modal>

      <Modal isOpen={successModal} toggle={toggleSuccessModal}>
        <ModalHeader toggle={toggleSuccessModal}>Success</ModalHeader>
        <ModalBody>
          Notification sent successfully!
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggleSuccessModal}>Close</Button>
        </ModalFooter>
      </Modal>
    </Container>
  );
};

export default NotificationForm;

