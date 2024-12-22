
import {
  Button,
  Checkbox,
  Flex,
  Text,
  FormControl,
  FormLabel,
  Heading,
  Input,
  Stack,
  Image,
  Box,
  Link,
} from '@chakra-ui/react'
import myImage from '../../assets/cat.png';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';
import { useAuth } from '../context/AuthContext.jsx';
import { errorNotification } from '../../services/notification';
import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

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

const LoginForm = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  return (
    <Formik
      validateOnMount={true}
      validationSchema={Yup.object({
        username: Yup.string()
          .max(20, 'Must be 20 characters or less')
          .required("Username is required"),
        password: Yup.string()
          .max(20)
          .min(6, 'Password must be at least 6 characters')
          .required("Password is required")
      })
        
      }
      initialValues={{username: '', password: ''} }
      onSubmit={(values, { setSubmitting }) => {
        setSubmitting(true);
        login(values).then(res => {
          navigate("/dashboard")
          console.log("Successfully  logged in");
        }).catch(err => {
          errorNotification(err.code, err.response.data.message)
        }).finally(() => {
          setSubmitting(false);
        })
      }}>
  
      {({ isValid, isSubmitting }) => (
        
        <Form>
          <Stack spacing={15}>
            <MyTextInput
              label={"Username"}
              name={"username"}
              type={"username"}
            />
            <MyTextInput
              label={"Password"}
              name={"password"}
              type={"password"}
              placeholder={"Type the password"}
            />
            <Button
              type={"submit"}
              disabled={!isValid || isSubmitting}>
              Login
              
            </Button>

          </Stack>

        </Form>

      )}
      
    </Formik>
  )
}

export default function Login() {

  const { person } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (person) {
      navigate("/dashboard");
    }
  })

  return (
    <Stack minH={'100vh'} direction={{ base: 'column', md: 'row' }}>
      <Flex p={8} flex={1} align={'center'} justify={'center'}>
        <Stack spacing={4} w={'full'} maxW={'md'}>
          <Heading fontSize={'2xl'} mb={15}>Sign in to your account</Heading>
          <LoginForm />
          <Link color={"blue.500"} href={'/signup'}>
          Don`t have an account? Signup now.
          </Link>
        </Stack>
      </Flex>
      <Flex flex={1}
        p={10}
        flexDirection={"column"}
        alignItems={"center"}
        justifyContent={"center"}
        bgGradient={{sm: 'linear(to-r, blue.600, purple.600)'}}
      >
        <Text fontSize={"6xl"} color={'white'} fontWeight={"bold"} mb={5}>
                  STool
        </Text>
        <Image
          alt={'Login Image'}
        objectFit={'scale-down'}
        src={myImage}
        />
      </Flex>
    </Stack>
  )
}