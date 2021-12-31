import React from 'react';
import { FieldValues, UseFormRegister } from 'react-hook-form';
import { translate, ValidatedFieldProps, ValidatedInput, ValidatedInputProps } from 'react-jhipster';
import { FormFeedback, FormGroup, Input } from 'reactstrap';

type OptionProp = {
  name: string;
  register: UseFormRegister<FieldValues>;
  error: any;
};

const OptionFormComponent = (props: OptionProp) => {
  const { name, register, error } = props;

  return (
    <>
      <FormGroup check>
        {/* Tạm thời lưu id ở đây sử dụng để update */}
        <ValidatedInput name={`${name}.id`} register={register} type="number" disabled hidden />
        <ValidatedInput name={`${name}.isCorrect`} register={register} type="checkbox" />
        <ValidatedInput
          name={`${name}.content`}
          register={register}
          validate={{
            required: { value: true, message: translate('entity.validation.required') },
          }}
          error={error}
          type="textarea"
        />
      </FormGroup>
    </>
  );
};

export default OptionFormComponent;
