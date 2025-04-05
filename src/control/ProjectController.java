package control;
import entity.*;
import java.util.List;

public class ProjectController {
    public Project getAvailableProjects (Applicant applicant){
        return applicant.getAppliedProject();
    }

    public boolean applyToProject (Applicant a, String projectName){
        if (a.isEligibleForProject())
    }
}
