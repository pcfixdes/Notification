import React, { useState } from 'react';
import { Form, FormGroup, Label, Input, Button, Modal, ModalHeader, ModalBody } from 'reactstrap';
import Multiselect from './Multiselect';

const Signup = ({ onSignup, toggle }) => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: '',
    phoneNumber: '',
    subscribedCategories: [],
    notificationChannels: []
  });
  const [modalOpen, setModalOpen] = useState(false);

  const handleChange = (name, value) => {
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const encodedPassword = btoa(formData.password);

    fetch('http://localhost:8080/api/users/signup', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ ...formData, password: encodedPassword }),
    })
      .then(response => {
        if (!response.ok) {
          throw new Error("Signup failed");
        }
        return response.json();
      })
      .then(data => {
        console.log('Signup success:', data);
        onSignup(data.id); 
        setModalOpen(true);
        setTimeout(() => {
          setModalOpen(false);
          toggle();
        }, 5000);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  };

  return (
    <>
      <Form onSubmit={handleSubmit}>
        <FormGroup>
          <Label style={{ fontWeight: 'bold' }} for="username">Username</Label>
          <Input
            type="text"
            id="username"
            name="username"
            value={formData.username}
            onChange={(e) => handleChange(e.target.name, e.target.value)}
            required
          />
        </FormGroup>
        <FormGroup>
          <Label style={{ fontWeight: 'bold' }} for="password">Password</Label>
          <Input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={(e) => handleChange(e.target.name, e.target.value)}
            required
          />
        </FormGroup>
        <FormGroup>
          <Label style={{ fontWeight: 'bold' }} for="email">Email</Label>
          <Input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={(e) => handleChange(e.target.name, e.target.value)}
            required
          />
        </FormGroup>
        <FormGroup>
          <Label style={{ fontWeight: 'bold' }} for="phoneNumber">Phone Number</Label>
          <Input
            type="text"
            id="phoneNumber"
            name="phoneNumber"
            value={formData.phoneNumber}
            onChange={(e) => handleChange(e.target.name, e.target.value)}
            required
          />
        </FormGroup>
        <Multiselect
          label="Subscribed Categories:"
          name="subscribedCategories"
          options={['Sports', 'Finance', 'Movies']}
          selectedOptions={formData.subscribedCategories}
          onChange={handleChange}
        />
        <Multiselect
          label="Notification Channels:"
          name="notificationChannels"
          options={['SMS', 'Email', 'Push']}
          selectedOptions={formData.notificationChannels}
          onChange={handleChange}
        />
        <Button type="submit" color="primary">Sign Up</Button>
        <Button color="secondary" onClick={toggle}>Cancel</Button>
      </Form>

      <Modal isOpen={modalOpen}>
        <ModalHeader>Success</ModalHeader>
        <ModalBody>
          Signup successful! Redirecting to login...
        </ModalBody>
      </Modal>
    </>
  );
};

export default Signup;
