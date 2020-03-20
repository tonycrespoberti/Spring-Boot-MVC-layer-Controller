/*
 * Author: Tony Crespo - tonycrespo@outlook.com
 * System Engineer, Java Spring MVC, Data, Boot, Cloud Developer
 */

package com.myapp.bricolaje.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myapp.bricolaje.model.repo.Cargo;
import com.myapp.bricolaje.model.service.GestorCargo;

@RestController
public class ControladorCargo {

	@Autowired
	private GestorCargo gestorCargo;
	
	
	//------------------
	
	
	/**
	 * Recurso encargado de dar de alta a nuevos cargos
	 * @param cargo Recibe del Body los datos del nuevo cargo
	 * @return Codigo de resultado: 
	 * 		0 si es correcto, 
	 *		1 sino hay especificado un Id, 
	 * 		2 sino hay descripcion, 
	 * 		3 si hubo problemas en persistirlo, 
	 * 		4 si ya existe uno previamente en la BBDD
	 */
	
	// por ejemplo: Body {"descripcion":"Supervisor"}
	@PostMapping(path = "/cargos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> altaCargo(@RequestBody Cargo cargo){
		
		int cargoResponse = gestorCargo.agregarCargo(cargo);
		
		if (cargoResponse == 0) {
			
			return new ResponseEntity<Integer>(0, HttpStatus.CREATED);
		}
		
		return new ResponseEntity<Integer>(cargoResponse, HttpStatus.BAD_REQUEST);
	}
	
	
	/**
	 * Recurso encargado de recuperar un objeto del tipo Cargo
	 * @param id	Recibe por PathVariable el Id del cargo a recuperar
	 * @return	El objeto Cargo recuperado o un Not Found
	 */
	@GetMapping(path = "/cargos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Cargo> obtenerCargo(@PathVariable("id") Integer id) {
		
		Optional<Cargo> optCargo = Optional.of(gestorCargo.obtenerCargo(id));
		
		if (optCargo.isPresent()) {
			
			return new ResponseEntity<Cargo>(optCargo.get(), HttpStatus.OK);
		
		}else {
			
			return new ResponseEntity<Cargo>(HttpStatus.NOT_FOUND);
		}
 	}
	
	
	/**
	 * Recurso encargado de modificar un Cargo
	 * @param cargo	Recibe el objeto de tipo Cargo por el Body
	 * @param id	Recibe el Id del objeto a modificar por Path
	 * @return	Devuelve 0 si es OK
	 * 			1 Si el Id recibido es inválido
	 * 			2 Si no encuentra el Id recibido en la BBDD
	 * 			3 Si ha habido un fallo durante la persistencia del cambio
	 */
	@PutMapping(path = "/cargos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> modificarCargo(@RequestBody Cargo cargo, @PathVariable Integer id){
		
		int cargoResponse = gestorCargo.modificarCargo(cargo);
		
		if (cargoResponse == 0) {
			
			return new ResponseEntity<Integer>(cargoResponse, HttpStatus.OK);
		
		}else {
		
			return new ResponseEntity<Integer>(cargoResponse, HttpStatus.BAD_REQUEST);
			
		}
	}
	
	
	/**
	 * Recurso encargado de eliminar un objeto de tipo Cargo
	 * @param id	Recibe por PathVariable el Id del cargo a eliminar de la BBDD
	 * @return	0 si se ha eliminado correctamente
	 * 			1 si el Id recibido es inválido
	 * 			2 Si no encuentra el Id en la BBDD
	 */
	@DeleteMapping(path = "/cargos/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Integer> eliminarCargo(@PathVariable Integer id){
		
		int cargoResponse = gestorCargo.eliminarCargo(id);
		
		if (cargoResponse == 0) {
			
			return new ResponseEntity<Integer>(cargoResponse, HttpStatus.OK);
		
		}else {
		
			return new ResponseEntity<Integer>(cargoResponse, HttpStatus.BAD_REQUEST);
			
		}
	}
	
	
	/**
	 * Recurso que gestiona la solicitud de lista de cargos bien sea porque contenga en la descripción del cargo, el valor
	 * recibido como parámtro, que proporcione la lista completa de cargos o que simplemente gestione la descripción de un cargo en
	 * específico.
	 * @param descripcion	Recibe la descrión o no del cargo a listar
	 * @param coincidenciaExacta	Recibe o no un valor de true si desea todas las descripciones de cargo que coincidan con la
	 * recibida o no.
	 * @return
	 */
	@GetMapping(path = "/cargos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Cargo>> listarCargos(@RequestParam(name = "descripcion", required = false) String descripcion,
			
			@RequestParam(name = "coincidenciaExacta", required = false) boolean coincidenciaExacta) {
		
		//por ejemplo http://localhost:8080/cargos?descripcion=supervisor
		if (descripcion != null) {
			
			//Por ejemplo http://localhost:8080/cargos?descripcion=supervisor?coincidenciaExacta=true
			if (coincidenciaExacta) {
				
				return new ResponseEntity<List<Cargo>>(gestorCargo.listCargosDescripcionExacta(descripcion), HttpStatus.OK);
				
			}else {
				
				return new ResponseEntity<List<Cargo>>(gestorCargo.listarCargoQueContengaDescripcion(descripcion), HttpStatus.OK);
				
			}
			
		}else {
			
			//Por ej: http://localhost:8080/cargos
			return new ResponseEntity<List<Cargo>>(gestorCargo.listarCargos(), HttpStatus.OK);
			
		}
		
	}
	
}
