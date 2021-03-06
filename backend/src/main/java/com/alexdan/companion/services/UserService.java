package com.alexdan.companion.services;

import com.alexdan.companion.data.DepartmentRepository;
import com.alexdan.companion.data.DocumentRepository;
import com.alexdan.companion.data.RoleRepository;
import com.alexdan.companion.data.UserRepository;
import com.alexdan.companion.exceptions.UserNotFoundException;
import com.alexdan.companion.models.Department;
import com.alexdan.companion.models.Document;
import com.alexdan.companion.models.Role;
import com.alexdan.companion.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DocumentRepository documentRepository;
    private final RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       DepartmentRepository departmentRepository,
                       DocumentRepository documentRepository,
                       RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.documentRepository = documentRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user != null)
            return user;
        throw new UsernameNotFoundException("User " + username + " not found");
    }

    public User getUser(long id){

        return userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(id));
    }

    public List<User> getAllUsers(){

        List<User> users = (List<User>) userRepository.findAll();
        return users.stream()
                .sorted(Comparator.comparing(User::getSurname))
                .collect(Collectors.toList());
    }

    public User updateUser(User user){

        User updUser = user;
        User oldUser = userRepository.findById(user.getId()).get();

        updUser.setRoles(oldUser.getRoles());
        updUser.setPassword(oldUser.getPassword());

        if ((!user.getDepartmentName().equals(oldUser.getDepartmentName())) || (oldUser.getDepartment() == null)) {
            updUser = save(user);
        } else {
            updUser = userRepository.save(user);
        }

        return updUser;
    }

    public User saveUser(User user){

        user.setRoles(Collections.singleton(roleRepository.save(new Role(1L,"ROLE_USER"))));
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return save(user);
    }

    public void deleteUser(Long id){

        User user = userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(id));

        if (!user.getDepartmentName().isEmpty()) {
              Department department = departmentRepository.findByName(user.getDepartmentName());
              department.deleteEmployee(user);
              departmentRepository.save(department);
          }

        userRepository.deleteById(id);
    }

    public User save(User user) {
        Department department;
        User savedUser;

        if (user.getDepartmentName() == null ||
                user.getDepartmentName().isEmpty()){
            user.setDepartmentName("");
            savedUser = userRepository.save(user);
            return savedUser;
        }

        department = departmentRepository.findByName(user.getDepartmentName());
        user.setDepartment(department);
        savedUser = userRepository.save(user);
        department.addEmployee(savedUser);
        departmentRepository.save(department);

        return savedUser;
    }

    public List<Document> getUsersDocuments(long id){

        return this.getUser(id).getDocuments();
    }

    public Document addFile(long id, Document document){
        User user = userRepository.findById(id).
                orElseThrow(()-> new UserNotFoundException(id));
        document.setUser(user);
        Document savedDocument = documentRepository.save(document);
        user.addDocument(savedDocument );
        userRepository.save(user);
        return savedDocument ;
    }
}
