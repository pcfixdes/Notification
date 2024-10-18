import React from 'react';
import { FormGroup, Label, Input } from 'reactstrap';

const Multiselect = ({ options, selectedOptions, onChange, label, name }) => {
  const handleCheckboxChange = (e) => {
    const { value, checked } = e.target;
    const updatedOptions = checked
      ? [...selectedOptions, value]
      : selectedOptions.filter(option => option !== value);
    onChange(name, updatedOptions);
  };

  return (
    <FormGroup>
      <Label style={{ fontWeight: 'bold' }}>{label}</Label>
      <div>
        {options.map((option, index) => (
          <Label  key={index} check style={{ display: 'flex', alignItems: 'center' }}>
            <Input
              type="checkbox"
              value={option}
              checked={selectedOptions.includes(option)}
              onChange={handleCheckboxChange}
              style={{ marginRight: '8px' }} // Adds space
            />
            {option}
          </Label>
        ))}
      </div>
    </FormGroup>
  );
};

export default Multiselect;
