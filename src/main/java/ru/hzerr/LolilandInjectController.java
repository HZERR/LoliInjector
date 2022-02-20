package ru.hzerr;

import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import ru.hzerr.file.BaseDirectory;
import ru.hzerr.file.BaseFile;
import ru.hzerr.file.HDirectory;
import ru.hzerr.file.HFile;
import ru.hzerr.util.SystemInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class LolilandInjectController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane root;

    @FXML
    private JFXRadioButton addWurst;

    @FXML
    private JFXRadioButton addCheatingEssentials;

    private BaseDirectory mods;
    private BaseDirectory yourMods;
    private static final ScheduledExecutorService injector = Executors.newSingleThreadScheduledExecutor();

    @FXML
    void onInject(ActionEvent event) {
        try {
            Map<BaseFile, Boolean> cheats = new HashMap<>();
            if (yourMods != null) cheats.putAll(yourMods.getFiles()
                    .filter(jar -> jar.getName().endsWith(".jar"))
                    .collect(Collectors.toMap(Function.identity(), jar -> false)));
            if (addWurst.isSelected()) cheats.put(HFile.from(new File(Objects.requireNonNull(LolilandInjectController.class.getResource("/" + Mods.FORGE_WURST.getName())).toURI())), false);
            if (addCheatingEssentials.isSelected()) cheats.put(HFile.from(new File(Objects.requireNonNull(LolilandInjector.class.getResource("/" + Mods.CHEATING_ESSENTIALS.getName())).toURI())), false);
            System.out.println("Injector was started");
            injector.scheduleAtFixedRate(() -> {
                cheats.forEach(new BiConsumer<BaseFile, Boolean>() {
                    @Override
                    public void accept(BaseFile baseFile, Boolean aBoolean) {
                        if (mods.getSubFile(baseFile.getBaseName()).notExists()) {
                            try {
                                baseFile.copyToDirectory(mods);
                            } catch (IOException e) {
                                System.out.println(baseFile.getBaseName() + " has been successfully injected");
                                cheats.put(baseFile, true);
                            }
                        }
                    }
                });
                if (!cheats.containsValue(false)) {
                    System.out.println("Success!");
                    injector.shutdownNow();
                    System.exit(0);
                }
            }, 0L, 200L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onInstallModsFolder(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        if (mods != null) {
            chooser.setInitialDirectory(mods.asIOFile());
        } else {
            File userHome = new File(SystemInfo.getUserHome());
            File lolilandFolder = userHome.toPath().resolve("loliland/updates/clients").toFile();
            chooser.setInitialDirectory(lolilandFolder.exists() ? lolilandFolder : userHome);
        }
        File target = chooser.showDialog(root.getScene().getWindow());
        if (target != null) mods = HDirectory.from(target);

    }

    @FXML
    void onInstallYourModsFolder(ActionEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        if (yourMods != null) chooser.setInitialDirectory(yourMods.asIOFile());
        File target = chooser.showDialog(root.getScene().getWindow());
        if (target != null) yourMods = HDirectory.from(target);
    }

    @FXML
    void initialize() {
        assert addWurst != null : "fx:id=\"addWurst\" was not injected: check your FXML file 'main.fxml'.";
        assert addCheatingEssentials != null : "fx:id=\"addCheatingEssentials\" was not injected: check your FXML file 'main.fxml'.";
        addWurst.selectedProperty().addListener((observable, o, n) -> {
            if (n) {
                addCheatingEssentials.setSelected(false);
            }
        });
        addCheatingEssentials.selectedProperty().addListener((observable, o, n) -> {
            if (n) {
                addWurst.setSelected(false);
            }
        });
    }
}
