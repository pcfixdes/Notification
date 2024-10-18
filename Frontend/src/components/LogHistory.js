import React from 'react';
import { ListGroup, ListGroupItem } from 'reactstrap';

const LogHistory = ({ logs }) => {
  return (
    <ListGroup>
      {logs.map((log, index) => (
        <ListGroupItem key={index}>
          <strong> 
            Categories:
            {' '} 
          </strong> 
          {log.subscribedCategories.join(', ')}
          {' '}
          <strong> 
            Message:
            {' '} 
          </strong> 
          {log.message} 
          {' '}
          <strong> 
            Date:
            {' '} 
          </strong> 
          <em>({new Date(log.timestamp).toLocaleString()})</em>
          {' '}
          <strong> 
            Channel:
            {' '} 
          </strong> 
           {log.channel}    
        </ListGroupItem>
      ))}
    </ListGroup>
  );
};

export default LogHistory;
