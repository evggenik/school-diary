import {  DeleteIcon, EditIcon } from '@chakra-ui/icons';
import {
  Card,
  CardBody,
  Heading,
  CardHeader,
  Flex,
  Avatar,
  IconButton,
  Box,
  Text,
  CardFooter,
  Button,
  Stack,
  StackDivider,
  ButtonGroup,
  AlertDialog,
  AlertDialogOverlay,
  AlertDialogContent,
  AlertDialogHeader,
  AlertDialogBody,
  AlertDialogFooter,
  useDisclosure
} from '@chakra-ui/react';

import React from 'react'
import { BsThreeDotsVertical } from 'react-icons/bs';
import { deleteTeacher } from '../services/client';
import { errorNotification, successNotification } from '../services/notification';
import EditTeacherDrawerForm from './EditTeacherDrawerForm';

const baseUrl = import.meta.env.VITE_API_BASE_URL;

const TeacherAlertDialog = ({ onDelete }) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()
  
    return (
      <>
        <Button colorScheme='red' leftIcon={<DeleteIcon />} onClick={onOpen}>
          Delete
        </Button>
  
        <AlertDialog
          isOpen={isOpen}
          leastDestructiveRef={cancelRef}
          onClose={onClose}
        >
          <AlertDialogOverlay>
            <AlertDialogContent>
              <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                Delete Teacher
              </AlertDialogHeader>
  
              <AlertDialogBody>
                Are you sure? You can't undo this action afterwards.
              </AlertDialogBody>
  
              <AlertDialogFooter>
                <Button ref={cancelRef} onClick={onClose}>
                  Cancel
                </Button>
                <Button colorScheme='red' onClick={() => {
                  onDelete(); // Вызов функции удаления
                  onClose(); // Закрытие диалога
                }}
                  ml={3}>
                  Delete
                </Button>
              </AlertDialogFooter>
            </AlertDialogContent>
          </AlertDialogOverlay>
        </AlertDialog>
      </>
    )
  }


export default function TeacherDetail({ teacher, fetchTeachers,
                                      onBackToList,
                                      setSelectedTeacher }) {
  const handleDelete = async () => {
    try {
        await deleteTeacher(teacher.id); // Удаление учителя по ID
      console.log('Teacher deleted successfully');
      successNotification(
        'Teacher deleted',
        `${teacher.firstName} was successfully deleted`
      );
      // Здесь вы можете обновить состояние или выполнить другие действия после успешного удаления
      fetchTeachers();
      onBackToList(); // Переключение на список учителей
    } catch (error) {
      console.error('Error deleting teacher:', error);
      errorNotification(
        err.code,
        err.response.data.message
      )
        // Обработка ошибки, например, уведомление пользователя
    }
  };
    // полный URL для аватара
    const avatarUrl = `${baseUrl}/${teacher.avatarUrl}`;
    return (
        <Flex
            direction="column"
            align="center" // Центрирование по вертикали
            justify="center" // Центрирование по горизонтали
        >
        <Card maxW='md'>
            <CardHeader>
                <Flex spacing='4'>
                    <Flex flex='1' gap='4' alignItems='center' flexWrap='wrap'>
                            <Avatar
                                name={`${teacher.firstName} ${teacher.lastName}`} 
                                src={avatarUrl} />

                        <Box>
                            <Heading size='sm'>{teacher.firstName} {teacher.lastName}</Heading>
                            <Text>{teacher.role}</Text>
                        </Box>
                    </Flex>
                    <IconButton
                        variant='ghost'
                        colorScheme='gray'
                        aria-label='See menu'
                        icon={<BsThreeDotsVertical />}
                    />
                </Flex>
            </CardHeader>
            <CardBody>
            <Stack divider={<StackDivider />} spacing='4'>
                <Text >
                    Email: <span style={{ marginLeft: '10px' }}>{teacher.email}</span>
                </Text>
                <Text >
                    Date of birth: <span style={{ marginLeft: '10px' }}>{teacher.birthDate}</span> 
                </Text>
                <Text >
                    Gender: <span style={{ marginLeft: '10px' }}>{teacher.gender}</span>
                </Text>
                </Stack>
            </CardBody>

            <CardFooter
                justify='space-between'
                flexWrap='wrap'
                sx={{
                '& > button': {
                    minW: '136px',
                },
                }}
            >
             <ButtonGroup variant='ghost' spacing='6'>
                {/* <Button  leftIcon={<EditIcon />}>
                    Edit
                </Button> */}
              
              <EditTeacherDrawerForm teacher={teacher}  
                // onUpdateTeacher={onUpdateTeacher}
                setSelectedTeacher={setSelectedTeacher}
              />

              <TeacherAlertDialog onDelete={handleDelete} fetchTeachers={fetchTeachers} />
        
            </ButtonGroup>
            
            </CardFooter>

        </Card>
        </Flex>
      );
}
