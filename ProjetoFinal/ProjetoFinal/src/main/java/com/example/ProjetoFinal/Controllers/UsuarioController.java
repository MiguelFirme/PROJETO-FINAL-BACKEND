package com.example.ProjetoFinal.Controllers;

import com.example.ProjetoFinal.Entidades.Carteira;
import com.example.ProjetoFinal.Entidades.Usuario;
import com.example.ProjetoFinal.Repositorys.CarteiraRepository;
import com.example.ProjetoFinal.Repositorys.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

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
    public ResponseEntity<List<Usuario>> listar() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable UUID id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        // salva usuário primeiro
        Usuario salvo = usuarioRepository.save(usuario);

        // cria carteira e associa
        Carteira carteira = new Carteira();
        carteira.setUsuario(salvo);
        carteira = carteiraRepository.save(carteira);

        // associa a carteira ao usuário para retorno consistente
        salvo.setCarteira(carteira);
        // opcional: salvar novamente se você quer que a FK em usuario seja populada
        usuarioRepository.save(salvo);

        // retorna 201 com location (opcional)
        URI location = URI.create("/api/usuarios/" + salvo.getId().toString());
        return ResponseEntity.created(location).body(salvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        if (!usuarioRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
