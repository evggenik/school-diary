
import { Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack } from '@chakra-ui/react';
import { Formik, Form, useField, useFormikContext } from 'formik';
import DatePicker, { registerLocale } from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import * as Yup from 'yup';

import { ru } from 'date-fns/locale';
import './createteacher-datepicker.css';
// import { px } from 'framer-motion';
import { newTeacher } from '../../services/client';
import AvatarInput from '../AvatarInput';
import { errorNotification, successNotification } from '../../services/notification';

registerLocale('ru', ru);
// setDefaultLocale('ru');

const MAX_FILE_SIZE = 1 * 1024 * 1024; // 1 MB

const SUPPORTED_FORMATS = ['image/jpg', 'image/jpeg', 'image/png'];

const MyTextInput = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Input className="text-input" {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
            <AlertIcon />
            {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};


const MySelect = ({ label, ...props }) => {
  const [field, meta] = useField(props);
  return (
    <Box>
      <FormLabel htmlFor={props.id || props.name}>{label}</FormLabel>
      <Select {...field} {...props} />
      {meta.touched && meta.error ? (
        <Alert className="error" status={"error"} mt={2}>
            <AlertIcon />
            {meta.error}
        </Alert>
      ) : null}
    </Box>
  );
};

export const DatePickerField = ({ ...props }) => {
    const { setFieldValue } = useFormikContext();
    const [field, meta] = useField(props);

    const minDate = new Date(); 
    const maxDate = new Date(); // Например, сегодня
    minDate.setFullYear(maxDate.getFullYear() - 60); // 60 лет назад

    return (
        <Box>
        <DatePicker
          {...field}
          {...props}
          selected={(field.value && new Date(field.value)) || null}
          onChange={val => {
            setFieldValue(field.name, val);
          }}
          locale="ru" // Устанавливаем локаль для компонента
          dateFormat="dd/MM/yyyy" // Укажите формат даты, если нужно
          showYearDropdown // Включаем выпадающий список для выбора года
          yearDropdownItemNumber={55} // Количество лет, отображаемых в выпадающем списке
          minDate={minDate} // Устанавливаем минимальную дату
          maxDate={maxDate} // Устанавливаем максимальную дату
          customInput={<Input placeholder="Выберите дату" />} // Используем Chakra UI Input
        />
        {meta.touched && meta.error ? (
          <Alert className="error" status={"error"} mt={2}>
              <AlertIcon />
              {meta.error}
          </Alert>
        ) : null}
      </Box>
    );
  };


// And now we can use these
const CreateTeacherForm = ({ fetchTeachers, onClose }) => {
  return (
    <>
      <Formik
        initialValues={{
          firstName: '',
          lastName: '',
          username: '',
          password: '',
          gender: '',
          email: '',
          role: '', 
          birthDate: null, // Добавляем поле для даты
          avatarFile: null, // Добавляем поле для файла
        }}
        validationSchema={Yup.object({
          firstName: Yup.string()
            .max(15, 'Must be 15 characters or less')
            .required('Required'),
          lastName: Yup.string()
            .max(20, 'Must be 20 characters or less')
            .required('Required'),
            username: Yup.string()
            .max(20, 'Must be 20 characters or less')
            .required('Required'),
            password: Yup.string()
            .min(6, 'Password must be at least 6 characters')
            .required('Required'),
            gender: Yup.string()
            .oneOf(
              ['MALE', 'FEMALE'],
              'Invalid gender'
            )
            .required('Required'),
          email: Yup.string()
            .email('Invalid email address')
            .required('Required'),
          role: Yup.string()
            .oneOf(
              ['TEACHER', 'STUDENT', 'PARENT', 'ADMINISTRATOR'],
              'Invalid Role'
            )
            .required('Required'),
            birthDate: Yup.date()
            .nullable()
            .required('Required'), // Добавляем валидацию для даты
            avatarFile: Yup.mixed()
            .nullable()
            .notRequired()
            .test('fileSize', 'File too large', value => {
              return value ? value.size <= MAX_FILE_SIZE : true; // Проверка размера файла
            })
            .test('fileFormat', 'Unsupported Format', value => {
              return value ? SUPPORTED_FORMATS.includes(value.type) : true; // Проверка типа файла
            })
        })}
        onSubmit={(teacher, { setSubmitting }) => {
          setSubmitting(true)
          newTeacher(teacher)
            .then(res => {
              console.log(res);
              successNotification(
                "Teacher saved",
                `${teacher.username} was successfully saved`
              )
              fetchTeachers();
              onClose();

            }).catch(err => {
              console.log(err)
              errorNotification(
                err.code,
                err.response.data.message
              )
            }).finally(() => {
            setSubmitting(false)
          })
        }}
      >
        
        <Form>
          <Stack spacing={"24px"}>
          <MyTextInput
            label="First Name"
            name="firstName"
            type="text"
            placeholder="First Name"
            id="firstName"
          />

          <MyTextInput
            label="Last Name"
            name="lastName"
            type="text"
            placeholder="Last Name"
            id="lastName"
          />

        <MyTextInput
            label="User Name"
            name="username"
            type="text"
            placeholder="user"
            id="username"
            />
            
        <MyTextInput
          label="Password"
          name="password"
          type="password" // Указываем тип как "password"
          placeholder="Enter your password"
          id="password"
        />

        <MySelect label="Gender" name="gender" id="gender">
            <option value="">Select gender</option>
            <option value="MALE">Male</option>
            <option value="FEMALE">Female</option>
        
          </MySelect>

          <MyTextInput
            label="Email Address"
            name="email"
            type="email"
            placeholder="email"
            id="email"
          />

          <MySelect label="Role" name="role" id="role">
            <option value="">Select a role type</option>
            <option value="TEACHER">Teacher</option>
            <option value="PARENT">Parent</option>
            <option value="STUDENT">Student</option>
            <option value="ADMINISTRATOR">Administrator</option>
          </MySelect>

          {/* Добавляем выбор даты */}
          <Box>
            <FormLabel htmlFor="birthDate">Date of Birth</FormLabel>
            <DatePickerField name="birthDate" />
            </Box>
            
            <AvatarInput label="Avatar" name="avatarFile" id="avatarFile"/>

          <Button type="submit">Submit</Button>
          </Stack>
        </Form>
      </Formik>
    </>
  );
};

export default CreateTeacherForm;