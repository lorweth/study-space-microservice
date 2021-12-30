import React from 'react';
import { ValidatedFieldProps, ValidatedInputProps } from 'react-jhipster';
import { FormFeedback, FormGroup, Input } from 'reactstrap';

export function WitcherValidatedInput({
  name,
  id = name,
  register,
  error,
  isTouched,
  isDirty,
  validate,
  children,
  className,
  onChange,
  onBlur,
  type,
  ...attributes
}: ValidatedInputProps): JSX.Element {
  className = className || '';
  className = isTouched ? `${className} is-touched` : className;
  className = isDirty ? `${className} is-dirty` : className;

  const { name: registeredName, onBlur: onBlurValidate, onChange: onChangeValidate, ref } = register(name, validate);
  return (
    <>
      <Input
        name={registeredName}
        id={id}
        valid={isTouched && !error}
        invalid={!!error}
        innerRef={ref}
        className={className}
        onChange={e => {
          void onChangeValidate(e);
          onChange && onChange(e);
        }}
        onBlur={e => {
          void onBlurValidate(e);
          onBlur && onBlur(e);
        }}
        {...attributes}
      >
        {type !== 'checkbox' && children}
      </Input>
      {error && <FormFeedback>{error.message}</FormFeedback>}
    </>
  );
}

function OptionComponent({
  children,
  name,
  id,
  disabled,
  className,
  row,
  col,
  tag,
  label,
  labelClass,
  labelHidden,
  inputClass,
  inputTag,
  hidden,
  register,
  ...attributes
}: ValidatedFieldProps): JSX.Element {
  return (
    <FormGroup check disabled={disabled} row={row} className={className} hidden={hidden} tag={tag}>
      <WitcherValidatedInput
        name={name}
        id={id}
        disabled={disabled}
        className={inputClass}
        hidden={hidden}
        tag={inputTag}
        {...attributes}
      ></WitcherValidatedInput>
    </FormGroup>
  );
}

export default OptionComponent;
