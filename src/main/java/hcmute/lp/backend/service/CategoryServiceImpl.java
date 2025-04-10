package hcmute.lp.backend.service;

import hcmute.lp.backend.model.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public List<Category> getCategories() {
        return List.of();
    }
}
