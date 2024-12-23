'use client'

import {
  Box,
  Flex,
  Avatar,
  HStack,
  Text,
  IconButton,
  Button,
  Menu,
  MenuButton,
  MenuList,
  MenuItem,
  MenuDivider,
  useDisclosure,
  useColorModeValue,
  Stack,
  VStack,
} from '@chakra-ui/react'
import { HamburgerIcon, CloseIcon } from '@chakra-ui/icons'
import { useAuth } from '../context/AuthContext.jsx'


const baseUrl = import.meta.env.VITE_API_BASE_URL;

const Links = ['Teachers', 'Students', 'Parents']

const NavLink = ({ children, onClick }) => {
  // const { children } = props
  

  return (
    <Box
      as="a"
      px={2}
      py={1}
      rounded={'md'}
      _hover={{
        textDecoration: 'none',
        bg: useColorModeValue('gray.200', 'gray.700'),
      }}
      // href={'#'}
      onClick={onClick}
      cursor="pointer"
      >
      {children}
    </Box>
  )
}

export default function Simple({ children, setShowTeachers, setSelectedTeacher }) {
  
  const { logOut, person } = useAuth();
  const { isOpen, onOpen, onClose } = useDisclosure();
  const handleNavLinkClick = (link) => {
    if (link === 'Teachers') {
      setShowTeachers(true);
      setSelectedTeacher(null); // Сбрасываем выбранного учителя
    } else {
      setShowTeachers(false); // Или сбрасываем состояние для других пунктов меню
    }
    onClose(); // Закрываем меню после клика
  };
  // console.log(`${baseUrl}/${person?.avatarUrl}`);
  return (
    <>
      
      <Box bg={useColorModeValue('gray.100', 'gray.900')} px={4}>
        <Flex h={16} alignItems={'center'} justifyContent={'space-between'}>
          <IconButton
            size={'md'}
            icon={isOpen ? <CloseIcon /> : <HamburgerIcon />}
            aria-label={'Open Menu'}
            display={{ md: 'none' }}
            onClick={isOpen ? onClose : onOpen}
          />
          <HStack spacing={8} alignItems={'center'}>
            <Box>SchoolTool</Box>
            <HStack as={'nav'} spacing={4} display={{ base: 'none', md: 'flex' }}>
              {Links.map((link) => (
                <NavLink key={link} onClick={() => handleNavLinkClick(link)}>
                {link}
              </NavLink>
                // <NavLink key={link}>{link}</NavLink>
              ))}
            </HStack>
          </HStack>
          <Flex alignItems={'center'}>
            <Menu>
              <MenuButton
                as={Button}
                rounded={'full'}
                variant={'link'}
                cursor={'pointer'}
                minW={0}>
                <Avatar
                  size={'sm'}
                  src={
                    `${baseUrl}/${person?.avatarUrl}`
                  }
                />
                <Text fontSize="sm">{person?.username}</Text>
                {person?.roles.map((role, id) => (
                  <Text key={id} fontSize="xs" color="gray.600">
                    {role}
                  </Text>
                ))}
                
              </MenuButton>
              <MenuList>
                <MenuItem>Link 1</MenuItem>
                <MenuItem>Link 2</MenuItem>
                <MenuDivider />
                <MenuItem onClick={logOut}>
                  Log out
                </MenuItem>
              </MenuList>
            </Menu>
          </Flex>
        </Flex>

        {isOpen ? (
          <Box pb={4} display={{ md: 'none' }}>
            <Stack as={'nav'} spacing={4}>
              {Links.map((link) => (
                <NavLink key={link} onClick={() => handleNavLinkClick(link)}>
                {link}
              </NavLink>
                // <NavLink key={link}>{link}</NavLink>
              ))}
            </Stack>
          </Box>
        ) : null}
      </Box>

      <Box p={4}>{children}</Box>
    </>
  )
}