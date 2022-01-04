import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { AsyncThunkAction } from '@reduxjs/toolkit';
import { useAppDispatch } from 'app/config/store';
import { AxiosResponse } from 'axios';
import React, { useEffect, useState } from 'react';
import { Button, ButtonDropdown, DropdownItem, DropdownMenu, DropdownToggle, Form, FormGroup, Input, InputGroup } from 'reactstrap';

type FindType = 'id' | 'name' | 'topic';

type PropType = {
  findById: any;
  findByName: any;
  findByTopic: any;
  findAll: any;
};

const SearchForm = (props: PropType) => {
  const dispatch = useAppDispatch();

  const { findById, findByName, findByTopic, findAll } = props;

  const [term, setTerm] = useState<string | number>();
  const [findType, setFindType] = useState<FindType>('id');
  const [inputType, setInputType] = useState<'text' | 'number'>('text');

  useEffect(() => {
    if (findType === 'id') {
      setInputType('number');
    } else {
      setInputType('text');
    }
  }, [findType]);

  const startSearching = e => {
    if (term) {
      switch (findType) {
        case 'id':
          dispatch(findById(term));
          break;
        case 'name':
          dispatch(findByName(term));
          break;
        case 'topic':
          dispatch(findByTopic(term));
          break;
        default:
          dispatch(findAll());
      }
    }
    e.preventDefault();
  };

  const clear = () => {
    setTerm(null);
    dispatch(findAll({}));
  };

  const handleFindBy = e => setFindType(e.target.value);

  const handleSearch = event => setTerm(event.target.value);

  return (
    <Form onSubmit={startSearching}>
      <FormGroup>
        <InputGroup>
          <Input type="select" name="findBy" id="findBy" style={{ maxWidth: '100px' }} onChange={handleFindBy}>
            <option value="id" style={{ maxWidth: '100px' }} hidden={!findById}>
              ID
            </option>
            <option value="name" style={{ maxWidth: '100px' }} hidden={!findByName}>
              Name
            </option>
            <option value="topic" style={{ maxWidth: '100px' }} hidden={!findByTopic}>
              Topic
            </option>
          </Input>
          <Input
            type={inputType}
            name="search"
            defaultValue={term}
            onChange={handleSearch}
            // placeholder={translate('studySpaceApp.groupStoreGroup.home.search')}
            placeholder="Tìm kiếm"
          />
          <Button className="input-group-addon">
            <FontAwesomeIcon icon="search" />
          </Button>
          <Button type="reset" className="input-group-addon" onClick={clear}>
            <FontAwesomeIcon icon="trash" />
          </Button>
        </InputGroup>
      </FormGroup>
    </Form>
  );
};
export default SearchForm;
