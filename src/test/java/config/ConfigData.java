package config;


/**
 *
 * @author stuartdd
 */
public class ConfigData implements Validatable {

    private String name;
    private Resources resources;
    private Functions functions;


    @Override
    public String validate(Object config) {
        ConfigData configData = (ConfigData)config;
        if (configData.getName()==null) {
            return "ConfigData name is null";
        }
        if (configData.getName().trim().length()==0) {
            return "ConfigData name is empty";
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Functions getFunctions() {
        return functions;
    }

    public void setFunctions(Functions functions) {
        this.functions = functions;
    }

 }
