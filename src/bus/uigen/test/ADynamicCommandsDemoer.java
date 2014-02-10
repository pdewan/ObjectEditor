package bus.uigen.test;

public class ADynamicCommandsDemoer {
	String[] animations = {"Bubble", "Merge"};
	public String[] getDynamicCommands() {
		return animations;
	}
	public void invokeDynamicCommand (String commandName) {
		System.out.println(commandName);
	}

}
