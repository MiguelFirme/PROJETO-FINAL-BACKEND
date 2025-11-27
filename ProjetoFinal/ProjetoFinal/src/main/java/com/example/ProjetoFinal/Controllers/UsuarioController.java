package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.DTOs.UsuarioRequest;
import com.example.ProjetoFinal.DTOs.UsuarioResponse;
import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Entidades.Usuario;
import com.example.ProjetoFinal.Exceptions.ResourceNotFoundException;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import com.example.ProjetoFinal.Repositorys.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final CarteiraRepository carteiraRepository;

    public UsuarioController(UsuarioRepository usuarioRepository,
                             CarteiraRepository carteiraRepository) {
        this.usuarioRepository = usuarioRepository;
        this.carteiraRepository = carteiraRepository;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> responseList = usuarioRepository.findAll().stream()
                .map(UsuarioResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
        return ResponseEntity.ok(new UsuarioResponse(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> criarUsuario(@Valid @RequestBody UsuarioRequest req) {
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(req.nome);
        novoUsuario.setEmail(req.email);
        novoUsuario.setSenha(req.senha);

        Usuario salvo = usuarioRepository.save(novoUsuario);

        Carteira carteira = new Carteira();
        carteira.setUsuario(salvo);
        carteira = carteiraRepository.save(carteira);

        //Atrelar carteira ao usuário automaticamente
        salvo.setCarteira(carteira);
        usuarioRepository.save(salvo);

        URI location = URI.create("/api/usuarios/" + salvo.getId().toString());
        return ResponseEntity.created(location).body(new UsuarioResponse(salvo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário", "id", id);
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
