package com.portfolio.backend.services;

import com.portfolio.backend.models.TeamMember;
import com.portfolio.backend.repositories.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;

    @Autowired
    public TeamMemberService(TeamMemberRepository teamMemberRepository) {
        this.teamMemberRepository = teamMemberRepository;
    }

    public List<TeamMember> getAllTeamMembers() {
        return teamMemberRepository.findAll();
    }

    public TeamMember createTeamMember(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

    public TeamMember updateTeamMember(Long id, TeamMember updatedTeamMember) {
        return teamMemberRepository.findById(id).map(teamMember -> {
            teamMember.setName(updatedTeamMember.getName());
            teamMember.setRole(updatedTeamMember.getRole());
            teamMember.setQuote(updatedTeamMember.getQuote());
            teamMember.setImageUrl(updatedTeamMember.getImageUrl());
            return teamMemberRepository.save(teamMember);
        }).orElseThrow(() -> new RuntimeException("TeamMember not found with id " + id));
    }

    public void deleteTeamMember(Long id) {
        teamMemberRepository.deleteById(id);
    }
}
