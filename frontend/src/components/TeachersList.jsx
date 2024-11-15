
import { 
  Card, 
  CardBody, 
  CardFooter, 
  CardHeader, 
  Divider, 
  Flex, 
  Heading, 
  Link, 
  Stack 
} from '@chakra-ui/react';
import React from 'react';
import DrawerForm from './DrawerForm';

export default function TeachersList({ teachers, onSelectTeacher, fetchTeachers }) {
  

  return (
    <Flex
            direction="column"
            align="center" // Центрирование по вертикали
            justify="center" // Центрирование по горизонтали
    >
      <Card variant={'outline'} colorScheme='blue' size={'md'} align='center'>
      <CardHeader>
      <Flex spacing='4'>
      <Heading size='md' textTransform='uppercase' color='blue.600' mb={2}>Teachers</Heading>
      </Flex>

            <Divider />

      </CardHeader>
      <CardBody >
    <Stack spacing={4} mt={-6}>
      {teachers.map((teacher) => (
        <Link 
          key={teacher.id} 
          onClick={() => onSelectTeacher(teacher)} 
        >
          {teacher.firstName} {teacher.lastName}
        </Link>
      ))}
    </Stack>
    </CardBody>
        <CardFooter>
      <DrawerForm fetchTeachers={fetchTeachers}/>  
    </CardFooter>
    </Card>
    </Flex>
  )
}
