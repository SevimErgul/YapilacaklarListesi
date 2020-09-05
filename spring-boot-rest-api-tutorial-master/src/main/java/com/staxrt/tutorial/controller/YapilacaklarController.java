package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.exception.ResourceNotFoundException;
import com.staxrt.tutorial.model.User;
import com.staxrt.tutorial.model.yapilacaklarliste;
import com.staxrt.tutorial.repository.YapilacaklarRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class YapilacaklarController {

  @Autowired
  private YapilacaklarRepository yapilacaklarRepository;

  /**
   * Get all users list.
   *
   * @return the list
   */
  @GetMapping("/listele")
  public List<yapilacaklarliste> getAllUsers() {
    return yapilacaklarRepository.findAll();
  }

  /**
   * Gets users by id.
   *
   * @param userId the user id
   * @return the users by id
   * @throws ResourceNotFoundException the resource not found exception
   */
  @GetMapping("/liste/{id}")
  public ResponseEntity<yapilacaklarliste> getUsersById(@PathVariable(value = "id") Long yapilacakId)
      throws ResourceNotFoundException {
	  yapilacaklarliste liste =
    	yapilacaklarRepository
            .findById(yapilacakId)
            .orElseThrow(() -> new ResourceNotFoundException("Bulunamadı  :: " + yapilacakId));
    return ResponseEntity.ok().body(liste);
  }

  /**
   * Create user user.
   *
   * @param user the user
   * @return the user
   */
  @PostMapping("/liste")
  public yapilacaklarliste yapilacaklarliste(@RequestBody yapilacaklarliste yapilacaklarliste) {
    return yapilacaklarRepository.save(yapilacaklarliste);
  }

  @PutMapping("/listeler/{id}")
  public ResponseEntity<yapilacaklarliste> listeGuncelle(
      @PathVariable(value = "id") Long yapilacakId, @Valid @RequestBody yapilacaklarliste yapilacaklarListesi)
      throws ResourceNotFoundException {

	  yapilacaklarliste yapilacaklarListe =
		yapilacaklarRepository
            .findById(yapilacakId)
            .orElseThrow(() -> new ResourceNotFoundException("Listede veri bulunamadı ID :: " + yapilacakId));

	  yapilacaklarListe.setYapilacak(yapilacaklarListesi.getYapilacak());
	  yapilacaklarListe.setDurum(yapilacaklarListesi.getDurum());
    final yapilacaklarliste listeyiGuncelle = yapilacaklarRepository.save(yapilacaklarListe);
    return ResponseEntity.ok(listeyiGuncelle);
  }

  @DeleteMapping("/liste/{id}")
  public Map<String, Boolean> deleteUser(@PathVariable(value = "id") Long yapilacakId) throws Exception {
    yapilacaklarliste liste =
    		yapilacaklarRepository
            .findById(yapilacakId)
            .orElseThrow(() -> new ResourceNotFoundException("Bulunamadı ID :: " + yapilacakId));

    yapilacaklarRepository.delete(liste);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}
