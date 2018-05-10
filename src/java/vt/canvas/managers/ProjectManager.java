/*
 * Created by Sait Tuna Onder on 2017.08.29  * 
 * Copyright © 2017 Sait Tun Onder. All rights reserved. * 
 */
package vt.canvas.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;
import vt.canvas.entityclasses.Project;
import vt.canvas.entityclasses.SimulationModel;
import vt.canvas.entityclasses.User;
import vt.canvas.sessionbeans.ProjectFacade;
import vt.canvas.sessionbeans.UserFacade;
import vt.canvas.sessionbeans.SimulationModelFacade;

/**
 *
 * @author Onder
 */
@Named(value = "projectManager")
@SessionScoped
public class ProjectManager implements Serializable {

    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private UploadedFile uploadedFile;

    /*
    The instance variable 'userFacade' is annotated with the @EJB annotation.
    The @EJB annotation directs the EJB Container (of the GlassFish AS) to inject (store) the object reference
    of the UserFacade object, after it is instantiated at runtime, into the instance variable 'userFacade'.
     */
    @EJB
    private UserFacade userFacade;

    @EJB
    private ProjectFacade projectFacade;

    @EJB
    private SimulationModelFacade simulationModelFacade;

    // Resulting FacesMessage produced
    FacesMessage resultMsg;

    private String newProjectName;
    private String uploadedFileName = "";

    private User user;

    // User Project List
    private List<Project> userProjects;
    // User Project Names List
    private List<String> userProjectNames;

    private String selectedProjectName = "";
    private String selectedProjectMap;
    private String simulationModelData = "";

    // Public Project List
    private List<Project> publicProjects;
    // Public Project Names List
    private List<String> publicProjectNames;

    private String selectedPublicProjectName = "";
    private String publicToMyProjectsProjectName = "";

    @PostConstruct
    public void init() {
        String user_name = (String) FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap().get("username");

        user = getUserFacade().findByUsername(user_name);

        // Get User Projects From Facade
        getUserProjectsFromFacade();
        // Get Public Projects From Facade
        getPublicProjectsFromFacade();

    }

    /**
     * Refresh all variables on page load
     */
    public void onload() {
        simulationModelData = "";
        selectedProjectName = "";
        selectedPublicProjectName = "";
        publicToMyProjectsProjectName = "";

    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    // Returns the uploaded uploadedFile
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    // Obtains the uploaded uploadedFile
    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }

    public ProjectFacade getProjectFacade() {
        return projectFacade;
    }

    public String getNewProjectName() {
        return newProjectName;
    }

    public void setNewProjectName(String projectName) {
        this.newProjectName = projectName;
    }

    public List<Project> getUserProjects() {
        return userProjects;
    }

    public void setUserProjects(List<Project> userProjects) {
        this.userProjects = userProjects;
    }

    public String getSelectedProjectName() {
        return selectedProjectName;
    }

    public void setSelectedProjectName(String selectedProjectName) {
        this.selectedProjectName = selectedProjectName;
    }

    public String getSelectedProjectMap() {
        return selectedProjectMap;
    }

    public void setSelectedProjectMap(String selectedProjectMap) {
        this.selectedProjectMap = selectedProjectMap;
    }

    public String getSimulationModelData() {
        return simulationModelData;
    }

    public void setSimulationModelData(String simulationModel) {
        this.simulationModelData = simulationModel;
    }

    public List<String> getUserProjectNames() {
        return userProjectNames;
    }

    public void setUserProjectNames(List<String> userProjectNames) {
        this.userProjectNames = userProjectNames;
    }

    public List<Project> getPublicProjects() {
        return publicProjects;
    }

    public void setPublicProjects(List<Project> publicProjects) {
        this.publicProjects = publicProjects;
    }

    public List<String> getPublicProjectNames() {
        return publicProjectNames;
    }

    public void setPublicProjectNames(List<String> publicProjectNames) {
        this.publicProjectNames = publicProjectNames;
    }

    public String getPublicToMyProjectsProjectName() {
        return publicToMyProjectsProjectName;
    }

    public void setPublicToMyProjectsProjectName(String publicToMyProjectsProjectName) {
        this.publicToMyProjectsProjectName = publicToMyProjectsProjectName;
    }

    public String getSelectedPublicProjectName() {
        return selectedPublicProjectName;
    }

    public void setSelectedPublicProjectName(String selectedPublicProjectName) {
        this.selectedPublicProjectName = selectedPublicProjectName;
    }

    /**
     * Gets Public Projects From Facade
     */
    private void getPublicProjectsFromFacade() {
        publicProjects = getProjectFacade().findPublicProjects();
        publicProjectNames = new ArrayList<>();
        for (int i = 0; i < publicProjects.size(); i++) {
            Project proj = publicProjects.get(i);
            publicProjectNames.add(proj.getProjectname());
        }
    }

    /**
     * Gets User Projects From Facade
     */
    private void getUserProjectsFromFacade() {
        userProjects = getProjectFacade().findProjectsByUserID(user.getId());
        userProjectNames = new ArrayList<>();
        for (int i = 0; i < userProjects.size(); i++) {
            userProjectNames.add(userProjects.get(i).getProjectname());
        }
    }

    /**
     * OnSelect Event for User Projects
     *
     * @param event
     */
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Project Selected!", event.getObject().toString()));
        selectedProjectName = event.getObject().toString();
    }

    /**
     * OnSelect Event for Public Projects
     *
     * @param event
     */
    public void onSelect2(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Public Project Selected!", event.getObject().toString()));
        selectedPublicProjectName = event.getObject().toString();
    }

    public String selectProject() {
        // If a project is not selected return
        if (selectedProjectName.equals("")) {
            resultMsg = new FacesMessage("Please select a project!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return "";
        }

        // Find the project
        Project project = projectFacade.findByProjectName(selectedProjectName).get(0);
        selectedProjectMap = Constants.FILES_RELATIVE_PATH + project.getMapname();

        // Find the simulation model of the project
        SimulationModel model = simulationModelFacade.findSimulationModelByProjectId(project.getId());
        if (model != null) {
            simulationModelData = model.getModeldata();
        } else {
            simulationModelData = "";
        }

        return "Simulator.xhtml?faces-redirect=true";
    }

    public void deleteProject() {
        if (selectedProjectName.equals("")) {
            resultMsg = new FacesMessage("Please select a project!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return;
        }

        List<Project> list = projectFacade.findByProjectName(selectedProjectName);
        if (list == null || list.isEmpty()) {
            resultMsg = new FacesMessage("Please select a project!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return;
        }
        Project project = list.get(0);
        getProjectFacade().remove(project);

        // Updates the current user and public project list
        getUserProjectsFromFacade();
        getPublicProjectsFromFacade();

        resultMsg = new FacesMessage("Project is deleted successfully!");
        FacesContext.getCurrentInstance().addMessage(null, resultMsg);

        selectedProjectName = "";
    }

    public void makeProjectPublic() {
        if (selectedProjectName.equals("")) {
            resultMsg = new FacesMessage("Please select a project!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return;
        }

        List<Project> list = projectFacade.findByProjectName(selectedProjectName);
        if (list == null || list.isEmpty()) {
            resultMsg = new FacesMessage("Please select a project!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return;
        }
        Project project = list.get(0);

        // If project is already public, return
        if (project.getPublicProject()) {
            resultMsg = new FacesMessage("Project is already Public!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            return;
        }

        // Set the project public
        project.setPublicProject(true);

        getProjectFacade().edit(project);

        // Updates the public project list
        getPublicProjectsFromFacade();

    }

    /**
     * Creates a copy of the selected public project and adds it into the user
     * project list
     */
    public void addToMyProjects() {

        if (publicToMyProjectsProjectName.equals("")) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Enter a Project Name!", ""));
            return;
        }

        if (selectedPublicProjectName.equals("")) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Select a Project!", ""));
            return;
        }

        List<Project> projectsFound = getProjectFacade().findByProjectName(publicToMyProjectsProjectName);
        if (projectsFound != null && !projectsFound.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Project Name already exists! Please Select another name!", ""));
            return;
        }

        Project proj = projectFacade.findByProjectName(selectedPublicProjectName).get(0);

        Project newProject = new Project(publicToMyProjectsProjectName, proj.getMapname(), user);
        getProjectFacade().create(newProject);

        SimulationModel model = simulationModelFacade.findSimulationModelByProjectId(proj.getId());
        SimulationModel newModel = new SimulationModel(model.getModeldata(), newProject);

        simulationModelFacade.create(newModel);

        // Update Current Projects Dynamically
        getUserProjectsFromFacade();

        publicToMyProjectsProjectName = "";
    }

    public String createProject() {

        if (uploadedFileName.equals("")) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Please Upload Background Map for the New Project!", ""));
            return "";

        }

        List<Project> projectsFound = getProjectFacade().findByProjectName(newProjectName);
        if (!projectsFound.isEmpty()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Project Name already exists! Please Select another name!", ""));
            return "";
        }

        Project newProject = new Project(newProjectName, uploadedFileName, user);
        getProjectFacade().create(newProject);

        selectedProjectName = newProjectName;
        selectedProjectMap = Constants.FILES_RELATIVE_PATH + uploadedFileName;

        // Clear Data
        uploadedFileName = "";
        newProjectName = "";

        // Update Current Projects Dynamically
        userProjects = getProjectFacade().findProjectsByUserID(user.getId());
        userProjectNames = new ArrayList<>();
        for (int i = 0; i < userProjects.size(); i++) {
            userProjectNames.add(userProjects.get(i).getProjectname());
        }

        // No Data For New Project
        simulationModelData = "";

        return "Simulator?faces-redirect=true";

    }

    public void saveProjectModel() {

        Project currentProject = projectFacade.findByProjectName(selectedProjectName).get(0);

        // If Project has an existing model, delete it
        SimulationModel model = simulationModelFacade.findSimulationModelByProjectId(currentProject.getId());
        if (model != null) {
            simulationModelFacade.remove(model);
        }

        model = new SimulationModel(simulationModelData, currentProject);
        simulationModelFacade.create(model);
    }

    /*
    ================
    Instance Methods
    ================
     */
    public void handleFileUpload(FileUploadEvent event) throws IOException {

        try {

            List<Project> projectsFound = getProjectFacade().findProjectsByUserID(user.getId());
            int projectCount = projectsFound.size();
            /*
            To associate the file to the user, record "filename" in the database.
            Since each file has its own primary key (unique id), the user can upload
            multiple files with the same name.
             */

            String filename = user.getId() + "_" + projectCount
                    + "_" + event.getFile().getFileName();

            /*
            "The try-with-resources statement is a try statement that declares one or more resources. 
            A resource is an object that must be closed after the program is finished with it. 
            The try-with-resources statement ensures that each resource is closed at the end of the
            statement." [Oracle] 
             */
            try (InputStream inputStream = event.getFile().getInputstream();) {

                // The method inputStreamToFile given below writes the uploaded file into the CloudStorage/FileStorage directory.
                inputStreamToFile(inputStream, filename);
                inputStream.close();
            }

            resultMsg = new FacesMessage("Model Background is Successfully Uploaded!");
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
            uploadedFileName = filename;

        } catch (IOException e) {
            resultMsg = new FacesMessage("Something went wrong during file upload! See: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, resultMsg);
        }

    }

    public void upload() throws IOException {

        if (getUploadedFile().getSize() != 0) {
            copyFile(getUploadedFile());
        }
    }

    /**
     * Used to copy a uploadedFile
     *
     * @param file
     * @return
     * @throws java.io.IOException
     */
    public FacesMessage copyFile(UploadedFile file) throws IOException {
        try {
            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            User user = getUserFacade().findByUsername(user_name);

            /*
            To associate the file to the user, record "userId_filename" in the database.
            Since each file has its own primary key (unique id), the user can upload
            multiple files with the same name.
             */
            String userId_filename = user.getId() + "_" + file.getFileName();

            /*
            "The try-with-resources statement is a try statement that declares one or more resources. 
            A resource is an object that must be closed after the program is finished with it. 
            The try-with-resources statement ensures that each resource is closed at the end of the
            statement." [Oracle] 
             */
            try (InputStream inputStream = file.getInputstream();) {

                // The method inputStreamToFile is given below.
                inputStreamToFile(inputStream, userId_filename);
                inputStream.close();
            }

            resultMsg = new FacesMessage("");  // No need to show a result message

        } catch (IOException e) {
            resultMsg = new FacesMessage("Something went wrong during file copy! See:" + e.getMessage());
        }

        // This sets the necessary flag to ensure the messages are preserved.
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);

        FacesContext.getCurrentInstance().addMessage(null, resultMsg);
        return resultMsg;
    }

    private File inputStreamToFile(InputStream inputStream, String file_name)
            throws IOException {

        // Read the series of bytes from the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Write the series of bytes on uploadedFile.
        File targetFile = new File(Constants.FILES_ABSOLUTE_PATH, file_name);

        OutputStream outStream;
        outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.close();

        return targetFile;
    }

    public String getFileLocation() {

        return Constants.FILES_ABSOLUTE_PATH;
    }

    /**
     * Used to return the file extension for a file.
     *
     * @param filename
     * @return
     */
    public static String getExtension(String filename) {

        if (filename == null) {
            return null;
        }
        int extensionPos = filename.lastIndexOf('.');

        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int lastSeparator = Math.max(lastUnixPos, lastWindowsPos);
        int index = lastSeparator > extensionPos ? -1 : extensionPos;

        if (index == -1) {
            return "";
        } else {
            return filename.substring(index + 1);
        }
    }

}
