package org.zerock.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.WebBoard;
import org.zerock.domain.WebReply;
import org.zerock.persistence.WebReplyRepository;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping("/replies/*")
public class WebReplyController {

	@Autowired
	private WebReplyRepository replyRepo;
	
	@Transactional
	@PostMapping("/{bno}")
	public ResponseEntity<List<WebReply>> addReply(
			@PathVariable("bno")Long bno,
			@RequestBody WebReply reply
			){
		
		log.info("addReply.......");
		log.info("BNO"+bno);
		log.info("REPLY:" + reply);
		
		WebBoard board = new WebBoard();
		board.setBno(bno);
		reply.setBoard(board);
		replyRepo.save(reply);
		
		return new ResponseEntity<>(getListByBoard(board),HttpStatus.CREATED);
	}
	
    private List<WebReply>	getListByBoard(WebBoard board)throws RuntimeException{
    	log.info("get list board...."+board);
    	return replyRepo.getRepliesOfBoard(board);
    }
    
    
    @Transactional
    @DeleteMapping("/{bno}/{rno}")
    public ResponseEntity<List<WebReply>> remove(@PathVariable("bno")Long bno,@PathVariable("rno")Long rno){
    	
    	
    	log.info("delete reply: "+rno);
    	replyRepo.deleteById(rno);
    	
    	WebBoard board = new  WebBoard();
    	board.setBno(bno);
    	
    	
    	return new ResponseEntity<>(getListByBoard(board), HttpStatus.OK);
    	
    }
    
    
    @Transactional
    @PutMapping("/{bno}")
    public ResponseEntity<List<WebReply>> modify(@PathVariable("bno")Long bno, @RequestBody WebReply reply){
    	
    	log.info("modify reply: "+reply);
    	replyRepo.findById(reply.getRno()).ifPresent(origin ->{
    		
    		origin.setReplyText(reply.getReplyText());
    		
    		replyRepo.save(origin);
    		
    		
    	});
    	
    	WebBoard board = new WebBoard();
    	board.setBno(bno);
    	
    	
    	return new ResponseEntity<>(getListByBoard(board),HttpStatus.CREATED);
    	
    	
    }
    
    
    @GetMapping("/{bno}")
    public ResponseEntity<List<WebReply>> getReplies(@PathVariable("bno")Long bno){
    	
    	log.info("get All replies");
    	WebBoard board = new WebBoard();
    	board.setBno(bno);
    	return new ResponseEntity<>(getListByBoard(board),HttpStatus.OK);
    }
    

	
}
