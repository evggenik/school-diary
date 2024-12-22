import { Flex, Heading, Image, Link, Stack, Text } from "@chakra-ui/react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import CreateTeacherForm from "../shared/CreateTeacherForm";
import myImage from '../../assets/cat.png';

export default function Signup() {
     
    const { person, setTeacherFromToken } = useAuth();
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
              <Heading fontSize={'2xl'} mb={15}>Register for an account</Heading>
                      <CreateTeacherForm onSuccess={(token) => { 
                          localStorage.setItem("access_token", token);
                          setTeacherFromToken();
                          navigate("/dashboard");
                      }}/>
              <Link color={"blue.500"} href={'/'}>
              Have an account? Login now.
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