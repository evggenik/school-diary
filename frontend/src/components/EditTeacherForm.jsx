import { Formik, Form, useField, useFormikContext } from 'formik';
import { Alert, AlertIcon, Box, Button, FormLabel, Input, Select, Stack } from '@chakra-ui/react';
import AvatarInput from './AvatarInput';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";

import * as Yup from 'yup';
import { fetchTeacherById, updateTeacher } from '../services/client';
import { errorNotification, successNotification } from '../services/notification';

const MAX_FILE_SIZE = 1 * 1024 * 1024; // 1 MB

const SUPPORTED_FORMATS = ['image/jpg', 'image/jpeg', 'image/png'];

const MyTextInput = ({ label, ...props }) => {
  // useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
  // which we can spread on <input>. We can use field meta to show an error
  // message if the field is invalid and it has been touched (i.e. visited)
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

  // Установите минимальную и максимальную даты
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

export default function EditTeacherForm({teacher, onClose, setSelectedTeacher}) {
    return (
        <>
          <Formik
            initialValues={{
              ...teacher, // Копируем все свойства объекта teacher
            }}
            validationSchema={Yup.object({
              
                userName: Yup.string()
                .max(20, 'Must be 20 characters or less')
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
          onSubmit={async (teacher, { setSubmitting }) => {
              // console.log("Before update " + teacher)
              setSubmitting(true)
              try {
                const res = await updateTeacher(teacher);
                console.log(res);
                successNotification(
                  "Teacher updated",
                  `${teacher.userName} was successfully updated`
                );
                  // Получаем обновленные данные конкретного пользователя
                const updatedTeacher = await fetchTeacherById(teacher.id);
                
            // onUpdateTeacher(updatedTeacher); // Обновляем состояние в родительском компоненте
                setSelectedTeacher(updatedTeacher); // Обновляем выбранного учителя
                onClose(); // Закрываем форму
          } catch (err) {
            console.log(err);
            errorNotification(err.code, err.response.data.message);
          } finally {
            setSubmitting(false);
          }
        }}
              
          >
            
            <Form>
              <Stack spacing={"24px"}>
              
            <MyTextInput
                label="User Name"
                name="userName"
                type="text"
                placeholder="user"
                id="username"
                />
    
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
}
