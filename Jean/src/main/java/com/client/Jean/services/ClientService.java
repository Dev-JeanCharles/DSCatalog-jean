package com.client.Jean.services;

import com.client.Jean.dto.ClientDTO;
import com.client.Jean.entities.Client;
import com.client.Jean.repositories.ClientRepository;
import com.client.Jean.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    ClientRepository repository;

    @Transactional(readOnly = true)
    public List<ClientDTO> findAll() {
        List<Client> list = repository.findAll();
        return list.stream().map(ClientDTO::new).collect(Collectors.toList());

    }
    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        Optional<Client> obj = repository.findById(id);
        Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not Found"));
        return new ClientDTO(entity);
    }

    @Transactional(readOnly = true)
    public ClientDTO insert(ClientDTO dto) {

        Client entity = new Client();
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setIncome(dto.getIncome());
        entity = repository.save(entity);
        return new ClientDTO(entity);
    }
}
