package pl.edu.ug.lab.wpluzek.projekt.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.edu.ug.lab.wpluzek.projekt.Domain.Furniture;
import pl.edu.ug.lab.wpluzek.projekt.Domain.Manufacturer;
import pl.edu.ug.lab.wpluzek.projekt.Repositories.FurnitureRepository;
import pl.edu.ug.lab.wpluzek.projekt.Repositories.ManufacturerRepository;

import java.util.List;

@RestController
@RequestMapping("/manufacturer")
public class ManufacturerController {

    @Autowired
    private FurnitureRepository furnitureRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @GetMapping
    public ModelAndView getAllManufacturers(Model model) {
        List<Manufacturer> manufacturersList = (List<Manufacturer>) manufacturerRepository.findAll();
        System.out.println(manufacturersList);
        model.addAttribute("manufacturersList", manufacturersList);
        return new ModelAndView("manufacturer/GetManufacturerList");
    }

    @GetMapping("/{manufacturerName}")
    public ModelAndView getManufacturerDetails(@PathVariable String manufacturerName, Model model) {
        Manufacturer manufacturer = manufacturerRepository.findByName(manufacturerName);
        List<Furniture> furnitureList = furnitureRepository.findByManufacturerName(manufacturerName);
        model.addAttribute("furnitureList", furnitureList);

        System.out.println(manufacturer.getId());
        model.addAttribute("manufacturer", manufacturer);
        return new ModelAndView("manufacturer/ManufacturerDetails");
    }

    @GetMapping("/{manufacturerName}/edit")
    public ModelAndView getManufacturerDetailsEdit(@PathVariable String manufacturerName, Model model) {
        Manufacturer manufacturer = manufacturerRepository.findByName(manufacturerName);
        List<Furniture> furnitureList = furnitureRepository.findByManufacturerName(manufacturerName);
        model.addAttribute("furnitureList", furnitureList);

        System.out.println(manufacturer.getId());
        model.addAttribute("manufacturer", manufacturer);
        return new ModelAndView("manufacturer/EditManufacturer");
    }

    @GetMapping("/add")
    public ModelAndView showAddManufacturerForm(Model model) {
        model.addAttribute("manufacturer", new Manufacturer());
        return new ModelAndView("manufacturer/AddManufacturer");
    }

    @PostMapping("/add")
    public String addManufacturer(@ModelAttribute Manufacturer manufacturer) {
        System.out.println(manufacturer.getName());
        manufacturerRepository.save(manufacturer);
        return "manufacturer_added";
    }

    @PutMapping("/{name}")
    public Manufacturer updateManufacturer(@PathVariable String name, @Validated @RequestBody Manufacturer manufacturerRequest) {
        Manufacturer manufacturer = manufacturerRepository.findByName(name);
        if (manufacturer == null) {
            throw new ResourceNotFoundException("Manufacturer not found with name " + name);
        }
        manufacturer.setEmail(manufacturerRequest.getEmail());
        manufacturer.setAddress(manufacturerRequest.getAddress());
        return manufacturerRepository.save(manufacturer);
    }



    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteManufacturer(@PathVariable String name) {
        Manufacturer manufacturer = manufacturerRepository.findByName(name);
        if (manufacturer == null) {
            throw new ResourceNotFoundException("Manufacturer not found with name " + name);
        }
        manufacturerRepository.delete(manufacturer);
        return ResponseEntity.ok().build();
    }

}

